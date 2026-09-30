#!/usr/bin/env python3
"""仓库一致性审计：BOM / 依赖声明 / 生成物 / 发布前置。

规范依据：
  - Docs/DevSpec/BomDependencySpec.md（§3.1 测试期构件、§3.3 ${revision}、§3.4 生成物、§3.5 差异台账）
  - Docs/DevSpec/GitHubActionWorkflowSpec.md §9（可靠性规范，§9.7 快速失败、§9.8 自动化守门）

用法：
  python .github/Python/AuditBomScopes.py --repo-root . [--mode audit|release] [--summary <file>]

检查项与强度（硬失败会让工作流失败；告警只输出提示）：
  C1  测试期构件不得被管理为 test 之外的 scope              硬失败
  C2  生成物 bom-aio 中本仓库构件的字面版本 vs 根 <revision>  release 态硬失败 / SNAPSHOT 态告警
  C3  跨 BOM scope/optional 差异与 BomDependencySpec 附录 A 台账比对  硬失败（台账缺失则 SKIP）
  C4  release 模式：待发布 POM 中不得出现 -SNAPSHOT 字面版本与 SNAPSHOT 版本号  硬失败
  C5  job 级 if: 是否覆盖 on: 的全部触发源（启发式，仅告警）                   告警

退出码：0 = 无硬失败（允许存在告警）；1 = 存在硬失败。
"""

import argparse
import glob
import os
import re
import sys
import xml.etree.ElementTree as ET

NS = "{http://maven.apache.org/POM/4.0.0}"

OS_DEPS_POM = "os-dependencies/pom.xml"
BOM_GLOB = "meta-bom/*/pom.xml"
GENERATED_BOM = "meta-bom/bom-aio/pom.xml"
REPO_GROUP_PREFIX = "com.acanx.meta"

LEDGER_DOC = "Docs/DevSpec/BomDependencySpec.md"
LEDGER_BEGIN = "<!-- BOM_SCOPE_BASELINE:BEGIN -->"
LEDGER_END = "<!-- BOM_SCOPE_BASELINE:END -->"
COLUMN_TO_FILE = {
    "os-dependencies": OS_DEPS_POM,
    "bom-deamon": "meta-bom/bom-deamon/pom.xml",
    "bom-mod": "meta-bom/bom-mod/pom.xml",
}

# 纯测试期构件（group 前缀）。判定口径见 BomDependencySpec §3.1：运行期零用途即计入。
TEST_LIB_GROUP_PREFIXES = (
    "org.junit",
    "org.opentest4j",
    "org.apiguardian",
    "org.mockito",
    "org.assertj",
    "org.hamcrest",
    "org.xmlunit",
)

FINDINGS = []  # [(severity, file, message)]
SKIPPED = []   # 显式记录「跳过」与其原因（GitHubActionWorkflowSpec §9.1 三态日志）

WORKFLOW_GLOBS = (".github/workflows/*.yml", ".github/workflows/*.yaml")
# 事件 → 该事件在表达式里可能出现的上下文（用于判断 job 级 if: 是否覆盖该事件）
#   schedule 没有 github.event.* 上下文，只能靠 event_name 判断；
#   因此「on: 含 schedule 且 if: 未提及 schedule」正是历史事故的形态（GitHubActionWorkflowSpec §9.3）
EVENT_CONTEXT_HINTS = {
    "workflow_dispatch": ("github.event.inputs",),
    "workflow_run": ("github.event.workflow_run",),
    "pull_request": ("github.event.pull_request",),
    "pull_request_target": ("github.event.pull_request",),
    "push": ("github.event.commits", "github.event.head_commit", "github.ref"),
    "schedule": (),
}


def report(severity, file, message):
    FINDINGS.append((severity, file, message))


def skip(check, reason):
    SKIPPED.append((check, reason))


def read_revision(repo_root):
    """从根 pom.xml 读取 <revision>（版本单一事实源，见 VersionReleaseSpec §1）。"""
    tree = ET.parse(os.path.join(repo_root, "pom.xml"))
    value = tree.getroot().findtext("./%sproperties/%srevision" % (NS, NS))
    return (value or "").strip() or None


def parse_dependency_management(path):
    """解析 <dependencyManagement>，返回 {(groupId, artifactId): {...}}。"""
    root = ET.parse(path).getroot()
    dm = root.find("./%sdependencyManagement" % NS)
    entries = {}
    if dm is None:
        return entries
    deps = dm.find("./%sdependencies" % NS)
    if deps is None:
        return entries
    for dep in deps.findall("./%sdependency" % NS):
        group = (dep.findtext("./%sgroupId" % NS) or "").strip()
        artifact = (dep.findtext("./%sartifactId" % NS) or "").strip()
        if not group or not artifact:
            continue
        entries[(group, artifact)] = {
            # 未声明 <scope> 与显式 compile 语义等价（BomDependencySpec §2.2）
            "scope": (dep.findtext("./%sscope" % NS) or "compile").strip(),
            "optional": (dep.findtext("./%soptional" % NS) or "").strip(),
            "version": (dep.findtext("./%sversion" % NS) or "").strip(),
            "type": (dep.findtext("./%stype" % NS) or "").strip(),
        }
    return entries


def bom_files(repo_root):
    """返回仓库内全部 BOM 文件的相对路径（含生成物）。"""
    files = [OS_DEPS_POM]
    files += sorted(os.path.relpath(p, repo_root) for p in glob.glob(os.path.join(repo_root, BOM_GLOB)))
    return files


def check_test_libs(repo_root):
    """C1：测试期构件必须以 test 声明，不得为 compile / provided / runtime。"""
    checked = 0
    for rel in bom_files(repo_root):
        for (group, artifact), info in parse_dependency_management(os.path.join(repo_root, rel)).items():
            if not group.startswith(TEST_LIB_GROUP_PREFIXES):
                continue
            if info["type"] == "pom":  # 导入型 BOM（如 junit-bom）不是依赖，跳过
                continue
            checked += 1
            if info["scope"] != "test":
                report(
                    "error",
                    rel,
                    f"测试期构件 {group}:{artifact} 被管理为 scope={info['scope']}，应为 test"
                    "（BomDependencySpec §3.1：未声明即 compile，会经 dependencyManagement 覆盖下游传递依赖）",
                )
    if checked == 0:
        skip("C1 测试期构件 scope", "未在 BOM 中找到测试期构件条目")
    return checked


def check_generated_versions(repo_root, revision):
    """C2：生成物 bom-aio 中本仓库构件的字面版本必须与根 <revision> 一致。"""
    if not os.path.exists(os.path.join(repo_root, GENERATED_BOM)):
        skip("C2 生成物版本一致性", f"{GENERATED_BOM} 不存在")
        return
    if not revision:
        skip("C2 生成物版本一致性", "未能从根 pom.xml 解析出 <revision>")
        return
    release_state = "-SNAPSHOT" not in revision
    checked = 0
    for (group, artifact), info in parse_dependency_management(os.path.join(repo_root, GENERATED_BOM)).items():
        if not group.startswith(REPO_GROUP_PREFIX):
            continue
        version = info["version"]
        if not version or "${" in version:
            continue  # 属性引用（如 ${revision}）天然自洽
        checked += 1
        if version == revision:
            continue
        if "-SNAPSHOT" in version and release_state:
            report(
                "error",
                GENERATED_BOM,
                f"{group}:{artifact} 固化版本 {version} 为 SNAPSHOT，而根 <revision> 已是 {revision}："
                "Maven Central 会拒绝整批 release 部署（BomDependencySpec §3.4，0.9.1 事故同因）",
            )
        else:
            report(
                "warning",
                GENERATED_BOM,
                f"{group}:{artifact} 固化版本 {version} 与根 <revision> {revision} 不一致："
                f"请在 <revision> 变更后重新执行 UpdateBOMAIODeps（BomDependencySpec §3.4）",
            )
    if checked == 0:
        skip("C2 生成物版本一致性", f"{GENERATED_BOM} 未出现本仓库构件的字面版本")


def parse_ledger(repo_root):
    """解析 BomDependencySpec 附录 A 台账，返回 {coord: {file: 归一值}}；不存在则返回 None。"""
    path = os.path.join(repo_root, LEDGER_DOC)
    if not os.path.exists(path):
        return None
    text = open(path, encoding="utf-8").read()
    match = re.search(re.escape(LEDGER_BEGIN) + r"(.*?)" + re.escape(LEDGER_END), text, re.S)
    if not match:
        return None

    rows = [line for line in match.group(1).strip().split("\n") if line.strip().startswith("|")]
    if len(rows) < 3:
        return None

    header = [cell.strip() for cell in rows[0].strip("|").split("|")]
    column_files = []
    for name in header[2:]:  # 前两列为「#」「坐标」
        key = name.strip("`")
        if key in COLUMN_TO_FILE:
            column_files.append(COLUMN_TO_FILE[key])
        else:
            return None

    ledger = {}
    for row in rows[2:]:
        cells = [cell.strip() for cell in row.strip("|").split("|")]
        if len(cells) < 2 + len(column_files):
            return None
        coord = cells[1].strip("`").strip()
        values = {}
        for file, cell in zip(column_files, cells[2:]):
            value = normalize_ledger_cell(cell)
            if value is not None:
                values[file] = value
        ledger[coord] = values
    return ledger


def normalize_ledger_cell(cell):
    """把台账单元格（如 `provided` / opt=true）归一为可与 POM 比对的形式。"""
    cell = cell.strip()
    if cell in ("", "—", "-", "N/A"):
        return None
    scope_match = re.search(r"`([^`]+)`", cell)
    scope = scope_match.group(1) if scope_match else cell.split("/")[0].strip()
    opt_match = re.search(r"opt=(\S+)", cell)
    return canonical_value(scope, opt_match.group(1) if opt_match else "")


def canonical_value(scope, optional):
    return f"{scope} / opt={optional or '-'}"


def canonical_from_info(info):
    return canonical_value(info["scope"], info["optional"])


def compute_scope_diff(repo_root):
    """计算跨 BOM 的 scope/optional 差异（不含生成物 bom-aio）。"""
    per_coord = {}
    for rel in bom_files(repo_root):
        if rel == GENERATED_BOM:
            continue
        for (group, artifact), info in parse_dependency_management(os.path.join(repo_root, rel)).items():
            if group.startswith("${") or artifact.startswith("${"):
                continue
            per_coord.setdefault(f"{group}:{artifact}", {})[rel] = canonical_from_info(info)
    return {
        coord: values
        for coord, values in per_coord.items()
        if len({value.split(" / opt=")[0] for value in values.values()}) > 1
    }


def check_ledger(repo_root):
    """C3：跨 BOM 差异必须与附录 A 台账一致（差异允许存在，但必须登记）。"""
    ledger = parse_ledger(repo_root)
    if ledger is None:
        skip("C3 跨 BOM 差异台账", f"未找到台账区间（{LEDGER_DOC} 的 BOM_SCOPE_BASELINE 注释），本项跳过")
        return
    actual = compute_scope_diff(repo_root)

    for coord in sorted(set(ledger) | set(actual)):
        expected = ledger.get(coord)
        found = actual.get(coord)
        if expected is None:
            detail = "; ".join(f"{f}={v}" for f, v in sorted(found.items()))
            report("error", LEDGER_DOC, f"发现未登记的新差异 {coord}（{detail}）：请在附录 A 台账登记后再提交")
            continue
        if found is None:
            report("error", LEDGER_DOC, f"台账中的 {coord} 实际已无不一致：请从附录 A 台账移除并说明结论")
            continue
        extra = set(found) - set(expected)
        if extra:
            report(
                "error",
                LEDGER_DOC,
                f"{coord} 的差异涉及台账未覆盖的文件 {sorted(extra)}：请扩展附录 A 台账列或修正声明",
            )
            continue
        for file in sorted(set(expected) | set(found)):
            if expected.get(file) != found.get(file):
                report(
                    "error",
                    file,
                    f"{coord} 的 scope/optional 与附录 A 台账不一致："
                    f"台账为 {expected.get(file) or '—'}，实际为 {found.get(file) or '—'}",
                )


def check_release_preflight(repo_root, revision):
    """C4（release 模式）：待发布 POM 不得含 SNAPSHOT 字面版本或 SNAPSHOT 版本号。"""
    if not revision:
        report("error", "pom.xml", "release 预检无法解析根 <revision>")
    elif "-SNAPSHOT" in revision:
        report("error", "pom.xml", f"release 预检失败：根 <revision> 仍为 {revision}，需先执行 UpdateProjectVersion")

    for rel in bom_files(repo_root):
        for (group, artifact), info in parse_dependency_management(os.path.join(repo_root, rel)).items():
            version = info["version"]
            if version and "${" not in version and "-SNAPSHOT" in version:
                report(
                    "error",
                    rel,
                    f"release 预检失败：{group}:{artifact} 版本为 {version}（SNAPSHOT）——"
                    "Maven Central 禁止 release 部署的 dependencyManagement 引用 SNAPSHOT 版本",
                )


def check_job_gates(repo_root):
    """C5（告警）：job 级 if: 是否覆盖 on: 声明的全部触发源。

    这是启发式检查，仅输出告警：job 级 if: 可能是有意的过滤（如仅 schedule 时执行），
    机器无法区分「有意过滤」与「遗漏导致静默跳过」，故由人工按 §9.3 确认。
    """
    try:
        import yaml  # noqa: PLC0415  —— 可选依赖，缺失时显式跳过而不报错
    except ImportError:
        skip("C5 门控覆盖性", "未安装 PyYAML，本项跳过（需 pip install --only-binary :all: pyyaml）")
        return

    paths = []
    for pattern in WORKFLOW_GLOBS:
        paths += glob.glob(os.path.join(repo_root, pattern))
    if not paths:
        skip("C5 门控覆盖性", "未找到工作流文件")
        return

    checked = 0
    for path in sorted(paths):
        rel = os.path.relpath(path, repo_root)
        try:
            doc = yaml.safe_load(open(path, encoding="utf-8"))
        except Exception as exc:  # noqa: BLE001 —— YAML 损坏时给出告警而不中断审计
            report("warning", rel, f"YAML 解析失败，C5 无法检查：{exc}")
            continue
        if not isinstance(doc, dict):
            continue
        # PyYAML 按 YAML 1.1 会把裸键 on 解析为布尔 True，两种写法都要兼容
        events = doc.get("on") if "on" in doc else doc.get(True)
        if events is None:
            continue
        if isinstance(events, str):
            events = [events]
        event_names = set(events.keys() if isinstance(events, dict) else events)

        for job_id, job in (doc.get("jobs") or {}).items():
            condition = str((job or {}).get("if") or "")
            if not condition:
                continue
            checked += 1
            mentioned = set(re.findall(r"github\.event_name\s*==\s*['\"]([a-z_]+)['\"]", condition))
            # 「不等于」写法（如 github.event_name != 'pull_request'）等价于「除该事件外全部覆盖」，
            # 常见于 SonarCloudCodeAnalysis.yml，若不识别会产生误报
            negated = set(re.findall(r"github\.event_name\s*!=\s*['\"]([a-z_]+)['\"]", condition))
            covered = mentioned | (event_names - negated if negated else set())
            uncovered = [
                event
                for event in sorted(event_names)
                if event not in covered
                and not any(hint in condition for hint in EVENT_CONTEXT_HINTS.get(event, ()))
            ]
            if uncovered:
                report(
                    "warning",
                    rel,
                    f"job `{job_id}` 的 if: 未覆盖触发源 {uncovered}（on: {sorted(event_names)}）——"
                    "若是有意过滤请在注释中说明；若是遗漏，该事件下的 run 会被整体静默跳过（§9.3）",
                )
    if checked == 0:
        skip("C5 门控覆盖性", "未发现带 job 级 if: 的工作流")


def emit(findings, summary_path):
    for severity, file, message in findings:
        prefix = "::error" if severity == "error" else "::warning"
        print(f"{prefix} file={file}::{message}")

    errors = [f for f in findings if f[0] == "error"]
    warnings = [f for f in findings if f[0] == "warning"]
    print(f"[审计结论] 硬失败 {len(errors)} 项，告警 {len(warnings)} 项，跳过 {len(SKIPPED)} 项")

    lines = ["## RepoConsistencyAudit 审计结果", ""]
    lines.append(f"- 硬失败：**{len(errors)}** 项")
    lines.append(f"- 告警：{len(warnings)} 项")
    lines.append(f"- 跳过：{len(SKIPPED)} 项")
    lines.append("")
    if errors or warnings:
        lines += ["| 级别 | 位置 | 说明 |", "| --- | --- | --- |"]
        for severity, file, message in findings:
            level = "❌ 硬失败" if severity == "error" else "⚠️ 告警"
            lines.append(f"| {level} | `{file}` | {message} |")
        lines.append("")
    if SKIPPED:
        lines += ["跳过项（显式记录原因，见 GitHubActionWorkflowSpec §9.1）：", ""]
        for check, reason in SKIPPED:
            lines.append(f"- `{check}`：{reason}")
        lines.append("")
    if not errors:
        lines.append("✅ 全部硬检查通过。")

    summary = "\n".join(lines) + "\n"
    if summary_path:
        with open(summary_path, "a", encoding="utf-8") as handle:
            handle.write(summary)
    print(lines[0])
    return 1 if errors else 0


def main():
    parser = argparse.ArgumentParser(description="MetaOpen 仓库一致性审计")
    parser.add_argument("--repo-root", default=".", help="仓库根目录")
    parser.add_argument("--mode", choices=["audit", "release"], default="audit", help="audit=常规审计；release=发布前置预检")
    parser.add_argument("--summary", default=None, help="Markdown 摘要写入路径（如 $GITHUB_STEP_SUMMARY）")
    args = parser.parse_args()

    repo_root = os.path.abspath(args.repo_root)
    revision = read_revision(repo_root)
    print(f"[审计参数] repo_root={repo_root} mode={args.mode} revision={revision}")

    check_test_libs(repo_root)
    check_generated_versions(repo_root, revision)
    check_ledger(repo_root)
    check_job_gates(repo_root)
    if args.mode == "release":
        check_release_preflight(repo_root, revision)

    sys.exit(emit(FINDINGS, args.summary))


if __name__ == "__main__":
    main()

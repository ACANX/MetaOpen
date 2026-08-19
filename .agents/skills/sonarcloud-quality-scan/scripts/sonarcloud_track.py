#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
SonarCloud → GitHub 追踪 Issue 生成器（可复用）

功能：从 SonarCloud 采集 OPEN/CONFIRMED issues，按实际问题类别分组，
生成每个类别一个 GitHub issue body（含决策清单 checkbox + 逐条详情：编号/规则/
严重度/位置/问题简述/官方提示/建议方案/本地代码摘录），支持用户逐条确认
（✅按建议修 / ❌不修 / 💡改方案）。

用法：
  python3 sonarcloud_track.py \
    --org <sonarcloud-org> \
    --project <sonarcloud-project-key> \
    --repo-dir <本地仓库路径> \
    --output-dir <输出目录> \
    [--categories categories.json]   # 自定义类别定义（缺省用内置）

输出：
  <output-dir>/<cat_key>.md        每类一个 issue body
  <output-dir>/meta.json           类别元数据（key/title/文件），供创建/更新 issue 用

依赖：Python3 标准库（urllib），无第三方依赖。
"""
import argparse
import json
import os
import sys
import urllib.request
from collections import defaultdict

# ============ 内置规则知识库：规则 -> (问题简述, 建议方案) ============
RULE_KB = {
    'githubactions:S7630': ("值直接插入 run 块，外部可控输入可注入任意命令",
                            "用户可控输入（inputs.*、github.head_ref 等）一律先写入 env 变量，run 块内用 $VAR 引用，禁止直接 ${{ }} 内插"),
    'githubactions:S7636': ("secrets 在 run 块内直接展开，日志/调试时可能泄露",
                            "secrets 先赋值给 env 变量（env 层），run 块内用 $VAR 引用"),
    'githubactions:S7637': ("第三方 Action 引用 @v1/@main 等可变引用，供应链风险",
                            "改用完整 40 位 commit SHA（如 actions/checkout@<sha>），配合 Dependabot 自动更新"),
    'githubactions:S8541': ("pip install 可能执行 setup.py 中的任意代码",
                            "pip 安装加 --only-binary :all: 参数"),
    'githubactions:S8544': ("依赖不锁定 resolved 版本，构建不可复现",
                            "用 requirements.txt 锁定或 pip-tools 生成锁定文件"),
    'java:S125': ("被注释的代码块应删除而非保留",
                  "确认无用则删除（git 历史可追溯）；确需保留参考的移入文档/说明，不留死代码"),
    'xml:S125': ("被注释的代码块应删除而非保留",
                 "确认无用则删除（git 历史可追溯）；确需保留参考的移入文档/说明，不留死代码"),
    'java:S3457': ("\\n 非平台无关换行，应使用 %n",
                   "格式化字符串中 \\n 替换为 %n"),
    'java:S3776': ("方法认知复杂度超阈值（默认 15），可读性差",
                   "拆分子方法（按职责拆分），控制分支嵌套；或项目级调高阈值（需确认）"),
    'java:S1118': ("纯静态工具类/常量类隐含 public 构造器，可被实例化",
                   "添加 private 构造器（可加注释：工具类禁止实例化）"),
    'java:S106': ("应使用日志框架而非 System.out",
                  "System.out.println → SLF4J Logger（参考仓库既有日志写法）"),
    'java:S6355': ("@Deprecated 注解应带 since 和/或 forRemoval",
                   "改为 @Deprecated(since = \"x.y.z\") 或加 forRemoval"),
    'java:S1123': ("废弃元素应加 Javadoc @deprecated 说明",
                   "在 Javadoc 中补充 @deprecated 标签及替代方案说明"),
    'java:S1133': ("标记 @Deprecated 的代码应计划移除",
                   "评估是否可移除；暂不移除则补全标注（since/说明），定期清理"),
    'java:S115': ("常量名不符合 ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$",
                  "常量改名全大写+下划线，同步更新引用"),
    'java:S1128': ("import 未使用",
                   "删除未使用的 import"),
    'java:S1066': ("嵌套 if 可合并为单条件",
                   "合并为 && 单层判断"),
    'java:S2209': ("通过实例引用访问静态成员",
                   "改为类名.静态成员直接访问"),
    'java:S1172': ("方法参数未被使用",
                   "确认后删除参数（注意调用方）或按需保留并加注释"),
    'java:S2440': ("应使用静态上下文而非实例化工具类",
                   "改为静态调用，移除实例化"),
    'java:S112': ("抛出过于通用的异常（如 Exception）",
                  "改用具体异常类型（如 IllegalArgumentException）"),
    'java:S5961': ("单方法断言数量超限",
                   "拆分为多个测试方法（按场景分组断言）"),
    'java:S1186': ("空方法体无说明",
                   "框架需要则加嵌套注释；否则删除（参考 CodeQualitySpec §2 决策树）"),
    'java:S1948': ("可序列化类含不可序列化字段（泛型/对象字段无约束）",
                   "DTO 移除 Serializable；异常类字段标 transient + 注释（参考 CodeQualitySpec §3）"),
    'java:S1192': ("字符串字面量重复 3 次以上",
                   "提取为常量（参考 CodeQualitySpec §4）"),
}

# ============ 内置默认类别定义 ============
DEFAULT_CATS = [
    ('SC', '[SonarCloud][工作流] 脚本注入与敏感信息加固（S7630/S7636/S8541/S8544）',
     ('githubactions:S7630', 'githubactions:S7636', 'githubactions:S8541', 'githubactions:S8544'),
     "## 类别说明\n\n- S7630：脚本注入，改经 env 传递\n- S7636：secrets 展开，改 env 引用\n- S8541/S8544：pip 安装安全"),
    ('SCF', '[SonarCloud][工作流] 第三方 Action 固定完整 SHA（S7637）',
     ('githubactions:S7637',),
     "## 类别说明\n\n- 第三方 Action 固定完整 SHA + 配合 Dependabot 自动更新"),
    ('SJ1', '[SonarCloud][Java] 清理注释掉的代码（S125）',
     ('java:S125', 'xml:S125'),
     "## 类别说明\n\n- 方案统一：确认无用删除（git 历史可追溯）；若某处是有意保留的参考，注明编号 ❌ 不修，或 💡 改为文档记录"),
    ('SJ2', '[SonarCloud][Java] 复杂度与格式（S3776/S3457）',
     ('java:S3776', 'java:S3457'),
     "## 类别说明\n\n- S3776：认知复杂度超标，需拆方法（建议单独 PR + 补测试）\n- S3457：\\n → %n"),
    ('SJ3', '[SonarCloud][Java] 工具类/常量类添加私有构造器（S1118）',
     ('java:S1118',),
     "## 类别说明\n\n- 方案统一：加 private 构造器（防实例化），快速修复"),
    ('SJ4', '[SonarCloud][Java] 日志与废弃代码规范（S106/S6355/S1123/S1133）',
     ('java:S106', 'java:S6355', 'java:S1123', 'java:S1133'),
     "## 类别说明\n\n- S106：System.out → Logger\n- S6355/S1123/S1133：@Deprecated 补全标注 or 移除废弃方法"),
    ('SJ5', '[SonarCloud][Java] 命名/import/逻辑杂项（S115/S1128/S1066/S2209/S1172/S2440/S112/S5961）',
     ('java:S115', 'java:S1128', 'java:S1066', 'java:S2209', 'java:S1172', 'java:S2440', 'java:S112', 'java:S5961'),
     "## 类别说明\n\n- 多为 1-2 行的小修复，可批量处理"),
]

# ============ 工具函数 ============

def fetch_issues(org, project):
    url = (f"https://sonarcloud.io/api/issues/search?organization={org}"
           f"&projects={project}&issueStatuses=OPEN%2CCONFIRMED&ps=500")
    with urllib.request.urlopen(url, timeout=30) as r:
        d = json.load(r)
    return d.get('issues', [])

def loc(i):
    return i['component'].split(':')[-1]

def short_path(p):
    parts = p.split('/')
    if len(parts) <= 2:
        return p
    return parts[-2] + '/' + parts[-1]

def read_snippet(repo, path, line, before=2, after=2):
    full = os.path.join(repo, path)
    if not os.path.exists(full):
        return None
    try:
        with open(full, encoding='utf-8') as f:
            lines = f.readlines()
        start = max(0, line - before - 1)
        end = min(len(lines), line + after)
        out = []
        for n in range(start, end):
            mark = '>>' if n == line - 1 else '  '
            out.append(f"{mark} {n+1}| {lines[n].rstrip()}")
        return '\n'.join(out)
    except Exception:
        return None

def read_line(repo, path, line):
    full = os.path.join(repo, path)
    if not os.path.exists(full):
        return None
    try:
        with open(full, encoding='utf-8') as f:
            lines = f.readlines()
        if 1 <= line <= len(lines):
            return lines[line-1].strip()
    except Exception:
        pass
    return None

def esc(s):
    return s.replace('|', '\\|') if s else s

def build_issue_body(cat_key, rules, extra_body, issues, repo):
    lst = [i for i in issues if i['rule'] in rules]
    lst.sort(key=lambda x: (x['component'], x['line']))
    rows, check_items = [], []
    for idx, i in enumerate(lst, 1):
        rule = i['rule']
        why, fix = RULE_KB.get(rule, (i['message'], '见官方提示'))
        p = loc(i)
        snip = read_snippet(repo, p, i['line'])
        if snip is None:
            snip = "_（本地未找到该文件/行，以 SonarCloud 为准）_"
        rows.append(
            f"### {cat_key}-{idx} `{rule}` {i.get('severity','')} · {p}:{i['line']}\n"
            f"- **问题**：{esc(why)}\n"
            f"- **官方提示**：{esc(i['message'])}\n"
            f"- **建议方案**：{esc(fix)}\n"
            f"- **代码摘录**（行 {i['line']} 附近）：\n```\n{snip}\n```\n")
        desc = why[:45]
        if rule in ('java:S125', 'xml:S125'):
            ln = read_line(repo, p, i['line'])
            if ln:
                desc = f"注释掉的代码（{ln.lstrip('-/* ').strip()[:40]}）"
        check_items.append(
            f"- [ ] `{cat_key}-{idx}` `{rule}` {i.get('severity','')} · `{short_path(p)}:{i['line']}` — {desc}")
    checklist = '\n'.join(check_items)
    body = f"""## 背景

来源：SonarCloud [查看全部](https://sonarcloud.io/project/issues?issueStatuses=OPEN%2CCONFIRMED&id=__PROJECT__)（OPEN/CONFIRMED）。本 issue 覆盖 **{len(lst)} 个**问题。

## 交互方式（重要）

请逐条回复你的决策，格式（直接在评论区回复，或编辑本 body 的决策清单）：

```
- {cat_key}-1 ✅ 按建议修
- {cat_key}-2 ❌ 不修，原因：保留作为参考
- {cat_key}-3 💡 改方案：<你的想法>
```

收到反馈后：✅ → 安排修复 PR（Closes 本 issue）；❌ → 在 SonarCloud 标记 Won't Fix/False Positive；💡 → 按新方案调整后再确认。

## 决策清单（待确认，勾选 = 按建议修）

{checklist}

{extra_body}

---

## 逐条详情

"""
    return body.replace('__PROJECT__', '') + '\n'.join(rows)

# ============ 主流程 ============

def main():
    ap = argparse.ArgumentParser(description='SonarCloud → GitHub 追踪 Issue 生成器')
    ap.add_argument('--org', required=True, help='SonarCloud organization')
    ap.add_argument('--project', required=True, help='SonarCloud project key')
    ap.add_argument('--repo-dir', required=True, help='本地仓库路径（用于代码摘录）')
    ap.add_argument('--output-dir', required=True, help='输出目录')
    ap.add_argument('--categories', default=None, help='自定义类别定义 JSON（可选）')
    args = ap.parse_args()

    if args.categories:
        with open(args.categories, encoding='utf-8') as f:
            cats = json.load(f)
        cats = [(c['key'], c['title'], tuple(c['rules']), c.get('extra', '')) for c in cats]
    else:
        cats = DEFAULT_CATS

    issues = fetch_issues(args.org, args.project)
    print(f"采集到 {len(issues)} 个 OPEN/CONFIRMED issues")

    os.makedirs(args.output_dir, exist_ok=True)
    meta = []
    for key, title, rules, extra in cats:
        body = build_issue_body(key, rules, extra, issues, args.repo_dir)
        fn = os.path.join(args.output_dir, f"{key}.md")
        with open(fn, 'w', encoding='utf-8') as f:
            f.write(body)
        meta.append({'key': key, 'title': title, 'file': fn, 'count': len([i for i in issues if i['rule'] in rules])})
        print(f"  [{key}] {title}: {meta[-1]['count']} 个 -> {fn}")

    with open(os.path.join(args.output_dir, 'meta.json'), 'w', encoding='utf-8') as f:
        json.dump(meta, f, ensure_ascii=False, indent=1)
    print(f"完成。meta: {os.path.join(args.output_dir, 'meta.json')}")
    print("下一步：gh issue create --title '<title>' --body-file <file>（新建）或 gh issue edit <num> --body-file <file>（更新）")

if __name__ == '__main__':
    main()

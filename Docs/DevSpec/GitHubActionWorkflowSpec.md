# GitHub Action 工作流编写规范（GitHubActionWorkflowSpec）

> 适用范围：MetaOpen 仓库 `.github/workflows/` 目录下的所有 GitHub Actions 工作流文件（`.yml` / `.yaml`）。
> 关联文档：[BomDependencySpec.md](./BomDependencySpec.md)（BOM / 依赖声明，含生成物规则）、[GitCommitPRSpec.md](./GitCommitPRSpec.md)、[PullRequestTargetAnalysis.md](../Dev/GitHubAction/PullRequestTargetAnalysis.md)
> 最后更新：2026-09-30

---

## 1. 文件命名规范

- **工作流文件名使用大驼峰（PascalCase）命名**，不使用连字符（`-`）、下划线（`_`）或空格。
- **文件名必须与工作流文件顶部的 `name` 属性值保持一致**（同名），便于文件与工作流的双向定位。
  - 正确示例：文件 `ReleaseWorkflow.yml` ↔ `name: ReleaseWorkflow`
  - 正确示例：文件 `UpdateBOMAIODeps.yml` ↔ `name: UpdateBOMAIODeps`
  - 错误示例：文件 `Foo.yml` 内部 `name: Bar` ❌（文件名与 name 不一致）
- 正确示例：
  - `ReleaseWorkflow.yml`
  - `UpdateBOMAIODeps.yml`
  - `MultiMavenJDKBranchCI.yml`
  - `SonarCloudCodeAnalysis.yml`
- 错误示例：
  - `release-workflow.yml` ❌
  - `update_bom_deps.yml` ❌
  - `my workflow.yml` ❌
- 扩展名统一使用 `.yml`（当前仓库存在 `.yaml` 历史文件，新文件一律使用 `.yml`）。
- 重命名工作流时，文件名与 `name` 属性需同步修改。

## 2. 工作流顶层 `name` 规范

- 工作流顶层 `name` 使用大驼峰命名，**不含空格**。
- 正确示例：`name: ReleaseWorkflow`
- 错误示例：`name: Release Workflow` ❌

## 3. Job / Step 的 `name` 规范

- **Job 的 `name`、Step 的 `name` 均使用大驼峰命名，中间不使用带空格的 name**。
- 正确示例：
  ```yaml
  jobs:
    release:
      runs-on: ubuntu-latest
      steps:
        - name: CheckoutCode          # ✅ 大驼峰、无空格
        - name: ExtractVersionFromPom # ✅ 大驼峰、无空格
        - name: CreateAndPushTag      # ✅ 大驼峰、无空格
  ```
- 错误示例：
  ```yaml
  steps:
    - name: Checkout repository       # ❌ 含空格
    - name: Build with Maven          # ❌ 含空格
    - name: Setup Java                # ❌ 含空格
  ```

### 命名要点

| 原则 | 说明 |
|------|------|
| 大驼峰 | 每个单词首字母大写，其余小写，如 `CreateGitHubRelease` |
| 无空格 | name 中不出现空格、连字符、下划线 |
| 语义清晰 | 名称应能概括步骤用途，如 `CheckIfTagExists`、`DryRunVerify` |
| 动词开头 | 步骤名以动词开头（Check / Create / Setup / Build / Publish 等） |

## 4. Step `id` 规范

- Step 的 `id` 使用**小驼峰（camelCase）**命名，与 `name` 风格区分。
- 正确示例：
  ```yaml
  - name: ExtractVersionFromPom
    id: version
  - name: CheckIfTagExists
    id: tag-check
  ```
- `id` 用于后续步骤引用（`steps.<id>.outputs.xxx`），命名应简洁且唯一。

## 5. 参考示例

`.github/workflows/ReleaseWorkflow.yml` 是符合本规范的推荐参考示例：

```yaml
name: ReleaseWorkflow                      # ✅ 顶层 name 大驼峰无空格

on:
  push:
    branches: [main]
  workflow_dispatch:
    inputs:
      dry_run:
        description: 'Dry run 模式：仅验证逻辑，不实际创建 tag 和 GitHub Release'
        type: boolean
        default: false

permissions:
  contents: write

jobs:
  release:                                 # ✅ job id 小驼峰
    runs-on: ubuntu-latest
    steps:
      - name: CheckoutCode                 # ✅ step name 大驼峰无空格
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: ExtractVersionFromPom        # ✅
        id: version
        run: |
          VERSION=$(grep -oP '(?<=<revision>)[^<]+' pom.xml | head -1 | tr -d ' \r\n')
          TAG="V${VERSION}"
          echo "version=${VERSION}" >> "$GITHUB_OUTPUT"
          echo "tag=${TAG}" >> "$GITHUB_OUTPUT"

      - name: CheckIfTagExists             # ✅
        id: tag-check
        run: |
          TAG="${{ steps.version.outputs.tag }}"
          if git rev-parse "$TAG" >/dev/null 2>&1; then
            echo "exists=true" >> "$GITHUB_OUTPUT"
          else
            echo "exists=false" >> "$GITHUB_OUTPUT"
          fi

      - name: DryRunVerify                 # ✅
        if: github.event.inputs.dry_run == 'true'
        run: echo "DRY RUN 验证模式"

      - name: CreateAndPushTag             # ✅
        if: steps.tag-check.outputs.exists == 'false' && github.event.inputs.dry_run != 'true'
        run: |
          git config user.name "github-actions[bot]"
          git config user.email "github-actions[bot]@users.noreply.github.com"
          git tag -a "${{ steps.version.outputs.tag }}" -m "Release ${{ steps.version.outputs.tag }}"
          git push origin "${{ steps.version.outputs.tag }}"

      - name: CreateGitHubRelease          # ✅
        if: steps.tag-check.outputs.exists == 'false' && github.event.inputs.dry_run != 'true'
        uses: softprops/action-gh-release@v2
        with:
          tag_name: ${{ steps.version.outputs.tag }}
          name: ${{ steps.version.outputs.tag }}
          generate_release_notes: true
```

## 6. 其他规范要点

| 项目 | 规范 |
|------|------|
| 触发方式 | `on:` 使用仓库内既有惯例；`workflow_dispatch` 的 `inputs` 命名用小驼峰（如 `dry_run`） |
| 权限 | 显式声明 `permissions:`，遵循最小权限原则（如 `contents: write`） |
| secrets | 使用 `${{ secrets.XXX }}`，禁止明文密钥 |
| shell 脚本 | 多行命令用 `run: \|` 块，保持可读性 |
| 条件执行 | 使用 `if:` 条件（如 `steps.xxx.outputs.xxx == 'true'`），避免整段脚本内判断 |
| 中文注释 | 关键步骤可加中文注释，与仓库交流语言（简体中文）一致 |
| 第三方 Action 引用 | **固定完整 commit SHA**（如 `crazy-max/ghaction-import-gpg@1c6a9e...`），不使用 tag（`@v3`）；tag 可被移动/删除，SHA 不可变（GitHub 官方安全加固建议，SonarCloud S7637）。版本维护由 Dependabot（github-actions 生态）自动更新 SHA |
| 官方 Action 引用 | `actions/*`、`github/*` 命名空间可继续用 tag（如 `actions/checkout@v5`），由 GitHub 官方维护 |

## 7. 检查清单

- [ ] 文件名大驼峰、`.yml` 扩展名
- [ ] **文件名与顶层 `name` 值一致**（同名）
- [ ] 顶层 `name` 大驼峰无空格
- [ ] 所有 job / step 的 `name` 大驼峰无空格
- [ ] step `id` 小驼峰且唯一
- [ ] 显式 `permissions`，最小权限
- [ ] 无明文密钥
- [ ] 每个「跳过」分支都有显式日志，无静默 `return` / `exit`（§9.1）
- [ ] 成功路径留有「处置了什么」的证据输出（§9.2）
- [ ] job 级 `if:` 已逐格核对，覆盖 `on:` 声明的全部触发源（§9.3）
- [ ] 判定失败前先复核对象状态，未把「别人已完成 / 正在做」当成失败（§9.4）
- [ ] 多触发源并发场景已声明 `concurrency:` 并说明取舍（§9.5）
- [ ] `gh` 参数、action `with:` 输入名已实测验证后在 PR 内提交（§9.6）
- [ ] 校验类检查已尽量前置，不在长流程末端才报错（§9.7）

## 8. 存量文件处理

仓库现有部分 workflow（如 `CodeQLAdvanced.yml`）的 step name 仍使用带空格写法。**存量文件不强制立即整改**，但满足以下条件之一时应同步改造为符合本规范：
- 对存量 workflow 进行功能性修改时
- 新增 step / job 时
- 涉及发布、版本管理的核心 workflow（`ReleaseWorkflow.yml`、`UpdateProjectVersion.yml` 等）

## 9. 可靠性规范

本节与 §1~§8 的「命名与形式」规范互补：命名规范保证**可读**，本节保证**可信**。

> **核心风险不是「跑挂了」，而是「跑绿了但没做事」**——静默失效。工作流一旦静默跳过，`success` 徽章会持续掩盖问题，直到很久之后以完全不同的形态爆发（真实案例：标签功能坏了 20 天、重命名失效 20 天、兜底任务从未运行）。以下 9.1~9.7 为强制条款，9.8 为机器守门。

### 9.1 三态日志（强制）

每一步必须能区分 **「做了 / 跳过了 / 失败了」** 三种结果，且**各自打印显式日志**：

- **禁止** `if (...) return;`、`exit 78`、`exit 0` 等静默放行路径而不打印原因；
- **禁止**用 `if:` 把整个 job 静默跳过（被门控跳过时应在日志中说明判定依据）。

```bash
# ✅ 正确：跳过时说明原因
if [ "$(get_pr_state "$pr")" != "OPEN" ]; then
  echo "[SKIP] PR #${pr} 状态为 ${state}（已被并发 run 处理），保持标签不变"
  return 0
fi

# ❌ 错误：静默返回
if ! matched; then return; fi
```

> 真实事故：`RenameDependabotBumpPRTitle` 的 `if (!matched) return;` → 根目录依赖 PR 的重命名失效 20 天，而每次 run 都是绿的（#2958）。

### 9.2 可观测的成功证据（强制）

成功的 run 必须留下「**我处置了什么**」的证据：处理了 N 个对象、改了哪些字段、结论是什么。只有 `success` 徽章而无处置痕迹的自动化，等于没有自动化。

```bash
# ✅ 正确：输出处置清单
core.info(`PR #${pr} 标题已重命名: ${title} → ${newTitle}`)
echo "本轮共处理 ${count} 个 PR：${list}"

# ❌ 错误：干完活什么都不说
await github.rest.pulls.update({ ... });
```

> 真实事故：标签与重命名两处都是「run 绿但没做事」，无人能从运行记录发现（#2761 / #2958）。

### 9.3 触发源与门控一致性（强制）

job 级 `if:` 必须覆盖 `on:` 声明的**全部**触发源。自查方法：把 `on:` 列出的事件逐个代入 `if:`，回答「该事件下这个条件为真吗」。

```yaml
# ❌ 错误：schedule 事件下 github.event.workflow_run 不存在，两个条件皆假 → 兜底任务永远被跳过
if: ${{ github.event_name == 'workflow_dispatch' || github.event.workflow_run.conclusion == 'success' }}

# ✅ 正确：显式覆盖每一个触发源
if: ${{ github.event_name == 'workflow_dispatch' || github.event_name == 'schedule' || github.event.workflow_run.conclusion == 'success' }}
```

> 真实事故：`AutoMergeDependencyUpgradeSuccessPR` 注释里写着「每 6 小时定时兜底」，但 `schedule` 触发的 run 全部被 `if:` 判假跳过，兜底从未生效。

### 9.4 幂等 + 先复核后判定（强制）

判定「失败」之前必须**先复核对象的真实状态**；「**别人已经做完 / 正在做**」不得判为失败。报错文本只能作为辅助判据，不能作为唯一判据。

```bash
if ! merge_output=$(gh pr merge "$pr" --squash 2>&1); then
  sleep 3                                   # 给并发方留出落地时间
  if [ "$(get_pr_state "$pr")" != "OPEN" ]; then
    echo "[SKIP] PR 已被并发 run 合并，按成功处理"   # 不得标记为失败
    return 0
  fi
  return 1                                  # 复核仍为 OPEN，才是真失败
fi
```

> 真实事故：AutoMerge 两个并发 run 同时合并同一 PR，后到者收到 `GraphQL: Merge already in progress` 被判为失败，于是把并发 run 刚打好的「成功」标签改写为「失败」——已合并的 PR 挂着「失败」标签，状态自相矛盾。

### 9.5 并发必须显式声明（强制）

同一工作流存在多触发源并发（如 `workflow_run` + `schedule` + `workflow_dispatch`）时，必须显式声明 `concurrency:` 并说明 `cancel-in-progress` 的取舍。

- GitHub 对同一 `group` **只保留一个 pending run**，新到的请求会替换掉先前 pending 的那个——因此「排队等待」不等于「一个不落」，必须配合 §9.4 的幂等复核；
- 未声明 `concurrency:` 时，多个 run 会同时处理同一对象，产生竞态。

### 9.6 外部动作先实测（强制）

`gh` 子命令的参数、第三方 action 的 `with:` 输入名**必须实测验证后再提交**，禁止凭记忆书写；PR 描述中附上实测输出（如 `gh pr edit --help`、一次真实执行的日志片段）。

> 真实事故：`gh pr edit --set-labels`（该参数根本不存在，`gh` 只有 `--add-label` / `--remove-label`）导致标签永远打不上（#2761）；`ghaction-import-gpg` 的输入名写成 `gpg-private-key`（正确为 `gpg_private_key`）被静默忽略。后者可由 §9.8 的 `actionlint` 自动拦下。

### 9.7 快速失败（强制）

便宜、确定的校验必须放在**成本最低的位置**（本地 / 前置步骤），不要等远端长流程末端才报错。

> 真实事故：`bom-aio` 的 SNAPSHOT 固化问题被留到 Maven Central 发布校验时才暴露，白等 6 分钟才拿到失败结论；若在发布工作流入口做一次本地扫描，30 秒内即可失败。

### 9.8 自动化守门（`RepoConsistencyAudit`）

仓库设 `RepoConsistencyAudit` 工作流（`pull_request` + `push` + `schedule`），对上述高危模式做机器检查：

| 检查项 | 强度 | 依据 |
| --- | --- | --- |
| 生成物版本一致性（`bom-aio` 中本仓库构件的字面版本 vs 根 `<revision>`） | release 态硬失败 / SNAPSHOT 态告警 | §9.7、[BomDependencySpec](./BomDependencySpec.md) §3.4 |
| 测试库不得被管理为 `compile` | 硬失败 | [BomDependencySpec](./BomDependencySpec.md) §3.1 |
| 跨 BOM scope/optional 差异与台账比对 | 硬失败 | [BomDependencySpec](./BomDependencySpec.md) §3.5 |
| `actionlint`（含 shellcheck：action 输入名、`if:` 表达式、`run` 脚本） | 硬失败 | §9.6 |
| 门控覆盖性启发式检查（`if:` 是否覆盖全部触发源） | **告警**（条件语义解析含启发式成分，稳定后再升级为硬检查） | §9.3 |

- **例外登记**：无法立即整改的既有问题，必须在该工作流内**显式登记为告警项**并在关联 issue 内跟踪，**不得静默放行**；
- 审计结果写入 job summary，便于在 PR 页面直接查看，无需翻日志。

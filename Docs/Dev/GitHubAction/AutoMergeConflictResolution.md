# 依赖升级 PR 合并冲突自愈机制技术方案（AutoMergeConflictResolution）

> 适用范围：`AutoMergeDependencyUpgradeSuccessPR` 工作流（依赖升级 PR 检查全绿后自动合并）的**合并失败/冲突处理**设计。
> 关联文档：[PullRequestTargetAnalysis.md](./PullRequestTargetAnalysis.md)（PR 事件机制）、[../Introduction](../Introduction)（项目文档索引）
> 最后更新：2026-08-24

---

## 1. 背景与目标

依赖升级 PR（Dependabot 的 `dependabot/**` 分支、CICD DependencyUpgrade 的 `ci/dependency-upgrade` 分支）统一以 **dependa** 为目标分支。多个升级 PR 并发存在时：

- 每个 PR 的 CI 检查全绿后，`AutoMergeDependencyUpgradeSuccessPR` 触发自动合并（squash）；
- 已通过 `concurrency` 配置保证**合并动作串行化**（一次只合并一个）；
- 但串行合并仍可能失败：**前一个 PR 合并后，后一个 PR 相对 dependa 变陈旧（stale）甚至文本冲突**。

本方案解决"合并失败后如何自动恢复并最终完成合并"，核心是 **update-branch 事件驱动自愈闭环**。

## 2. 术语

| 术语 | 说明 |
|------|------|
| U1 | 依赖升级分支（如 `dependabot/maven/dependa/xxx`、`ci/dependency-upgrade`） |
| base | PR 的目标分支（本场景为 `dependa`） |
| 陈旧（stale） | base 有新提交但 head 与 base 无文本冲突，可直接更新分支 |
| 文本冲突 | head 与 base 修改了同一处代码/配置（如 pom.xml 同一行），Git 无法自动合并 |

## 3. 总体机制：事件驱动自愈闭环

```
U1 → dependa 合并失败（409 Conflict）
        │
        ▼
┌─────────────────────────────────────────────┐
│ 自动执行 gh pr update-branch U1（见 §4）      │
│   = 把 dependa 最新提交 合并/rebase 进 U1     │
└─────────────────────────────────────────────┘
        │
        ├── ✅ 成功：U1 产生新 commit
        │        → 触发 pull_request(synchronize) → CICD 重跑
        │        → 检查全绿 → workflow_run 再次触发 AutoMerge
        │        → 再次尝试合并（此时基于最新 dependa，成功率高）
        │
        └── ❌ 失败（文本冲突无法自动解决）
             → 打印中文日志 + 退出（exit 1），交给人工处理（见 §6）
```

**关键点**：`update-branch` 产生的 commit 会触发 CI 重跑，而 CI 完成又会触发 AutoMerge —— 由事件自然驱动重试，**无需轮询、无需定时器**。

## 4. `gh pr update-branch` 详解（回答核心疑问）

### 4.1 命令行为

```bash
gh pr update-branch <PR号> --repo ACANX/MetaOpen
# 或指定 head 分支名
gh pr update-branch <U1分支名> --repo ACANX/MetaOpen
```

官方语义：**"Update the branch of a pull request to the latest version of its base branch"** —— 把 PR 的 **base 分支**（不是任意分支）最新提交合并进 head 分支。

### 4.2 基于哪个 base？—— **dependa，不是 dev**

U1 → dependa 的 PR，其 base 是 **dependa**，因此：

- `gh pr update-branch U1` 实际执行 ≈ `git checkout U1 && git merge origin/dependa`（默认 merge 方式）
- 或 `gh pr update-branch U1 --rebase` ≈ `git rebase origin/dependa`（rebase 方式）
- 更新后 push 回 U1 远程分支

**为什么不是 dev**：PR 的合并对象是 dependa。若以 dev 为基准更新 U1，会把 dev 上无关的变更带进依赖升级 PR，扩大合并面、引入无关冲突。**update-branch 永远以该 PR 的 base 分支为基准**（GitHub 平台行为，无法也不应指定其他分支）。

> ⚠️ dev → dependa 的同步属于**另一条流程**（定期合并 / 手动合并），与"更新 U1"无关。

### 4.3 内部操作与触发链

| 步骤 | 操作 | 结果 |
|------|------|------|
| 1 | 读取 PR 的 base 分支最新提交 | 拿到 dependa HEAD |
| 2 | 在 head 分支（U1）执行 merge（默认）或 rebase（`--rebase`） | U1 本地产生新 commit（或无变化） |
| 3 | push 到 U1 远程分支 | 触发 `pull_request` 的 `synchronize` 事件 |
| 4 | CI 工作流在**新的 merge commit** 上重跑 | 检查全绿后可再次合并 |

### 4.4 适用限制

- **同仓库分支**（U1 在 ACANX/MetaOpen 内）：✅ 直接可用（Dependabot 分支、ci/dependency-upgrade 均属此类）
- **fork 分支**：需 PR 勾选 *Allow edits by maintainers*（允许维护者修改），否则 `update-branch` 无权限推送，会失败退出
- 文本冲突时 merge/rebase 无法自动完成 → 命令失败（非零退出码），不会产生半成品 commit

## 5. 冲突类型与处理策略

| 类型 | 表现 | 处理 |
|------|------|------|
| **陈旧（stale）** | base 有新提交，但无文本冲突（如不同依赖块、不同行） | `update-branch` 自动解决 ✅ 无需人工 |
| **文本冲突** | 与 base 修改同一处（如多个 PR 都改 pom.xml 同一属性行） | `update-branch` 失败 → 人工介入（见 §6） |

> 依赖升级 PR 的冲突高发区：**pom.xml 的属性行**（`<properties>` 里共用的版本变量，如 `spring.version`）。不同依赖的 `<dependency>` 块通常在不同行，Git 可自动合并，不会冲突。

## 6. 防"一直失败"机制（不会无限重试）

### 6.1 幂等性（天然终止条件）

- `update-branch` 在 U1 **已是最新**（与 dependa 无差异）时不产生任何 commit → **不触发 CI** → 不再次触发 AutoMerge → 循环自然终止。
- 只有"确实落后于 base"时才会更新并触发下一轮。

### 6.2 失败即退出（不盲目重试）

- 文本冲突：`update-branch` 失败 → 打印中文日志 + `exit 1`，工作流结束（job 变红便于发现），**不会在同一 run 内死循环**。
- 人工介入方式：解决 U1 上的冲突并 push → 自动触发 CI → 检查全绿后 AutoMerge 再次自动接管。

### 6.3 合并动作串行化（防竞争放大）

`AutoMergeDependencyUpgradeSuccessPR` 已配置：

```yaml
concurrency:
  group: automerge-dependency-prs
  cancel-in-progress: false
```

所有合并/更新动作排队执行，避免"A 合并的同时 B 在更新/合并"造成的二次竞争。

### 6.4 可选增强（建议实现）

**合并完成后批量更新排队 PR**：在同一个 concurrency 队列内，A 合并成功后，立即对所有仍 open 的依赖升级 PR 执行一次 `gh pr update-branch`，让它们提前 rebase 到最新 dependa，CI 重跑后逐个合并 —— 从源头减少"陈旧 → 冲突"的概率。

## 7. 建议的 AutoMerge 合并失败分支实现（伪代码）

```bash
if merge_output=$(gh pr merge "$pr_number" --repo "$REPO" --squash 2>&1); then
  echo "🎉 PR #${pr_number} 已自动合并完成！"
else
  echo "❌ 合并失败（可能冲突/陈旧），尝试自动更新分支..."
  echo "$merge_output"
  if gh pr update-branch "$pr_number" --repo "$REPO" 2>&1; then
    echo "✅ 已基于 base（dependa）更新分支，等待 CI 重跑后自动再次合并"
    # 更新触发 CI → workflow_run 再次触发本工作流 → 自动重试（§3 闭环）
  else
    echo "❌ 更新分支失败（文本冲突，需人工解决），任务退出。"
    exit 1
  fi
fi
```

> 说明：闭环的重试由 `workflow_run`（CICD 完成事件）自然驱动，**不需要**在同一 run 内 sleep/循环重试。

## 8. 与其他机制的协同

| 机制 | 作用 | 状态 |
|------|------|------|
| `concurrency` 串行化 | 合并/更新动作排队，防竞争 | ✅ 已实施（PR #2652） |
| Dependabot `rebase-strategy: auto`（默认） | base 更新后 Dependabot **自动 rebase 自己的 PR** 并重跑 CI | ✅ 平台默认生效 |
| Dependabot `groups` | 关联依赖合并为少数 PR，从源头减少冲突面 | 建议启用 |
| Branch protection "Require up-to-date" | 平台级强制：陈旧 PR 禁止合并 | 建议启用 |
| 合并后批量 update 排队 PR（§6.4） | 提前消除陈旧，减少冲突 | 建议实施 |

## 9. 参考

- `gh pr update-branch` 帮助：`gh pr update-branch --help`
- GitHub CLI 手册：https://cli.github.com/manual/gh_pr_update-branch
- 关联：`AutoMergeDependencyUpgradeSuccessPR.yml`、`CICD.yml`（DependencyUpgrade job）、`dependabot.yml`

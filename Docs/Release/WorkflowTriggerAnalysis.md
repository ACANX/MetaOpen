# 首次合并触发 ReleaseWorkflow 问题探究

> **主题**：ReleaseWorkflow 文件首次进入 main 分支（即第一次 dev→main 合并）时，`push` 事件能否触发该 workflow？
>
> 关联文档：[README.md](./README.md)（发布方案总览）
> 最后更新：2026-08-19

---

## 1. 问题背景

ReleaseWorkflow（`.github/workflows/ReleaseWorkflow.yml`）通过 `on: push: branches: [main]` 触发。它由 PR 合入 **dev** 分支，再随 dev→main 合并进入 **main**（默认分支）。

由此产生一个疑问：**第一次 dev→main 合并时，workflow 文件才刚刚进入 main，`push` 事件是否会被 GitHub 识别并触发？还是必须等到下一次发版（文件已在 main 上）才会触发？**

---

## 2. 结论（先给答案）

> ✅ **能触发，无需等待下一个版本。**
>
> 第一次 dev→main 合并（workflow 文件随该次 push 首次进入 main）时，`push` 事件会立即触发 ReleaseWorkflow。

---

## 3. 原理分析

### 3.1 GitHub Actions 的 push 事件语义

`push: branches: [main]` 的触发判定，基于**该次 push 完成之后的分支状态**：

1. 开发者推送提交到 main
2. GitHub 接收 push，更新 main 分支引用
3. GitHub **重新扫描** main 分支上的 `.github/workflows/` 目录
4. 若发现匹配的 workflow 文件（包括**本次 push 刚新增的**），触发对应 workflow

即：GitHub 不是"用 push 之前的分支状态"判断，而是"用 push 之后的分支状态"判断。因此 workflow 文件随 push 首次出现时，该 push 本身就会触发它。

### 3.2 与 workflow_dispatch 的区别（重要）

| 触发方式 | 是否要求 workflow 已存在于默认分支 | 说明 |
|----------|-----------------------------------|------|
| `push` | ❌ 不要求 | 只要 push 后分支上有该文件即可触发 |
| `workflow_dispatch` | ✅ **要求** | 手动触发前，GitHub 需先在默认分支发现该 workflow，否则 API 返回 404 |

**推论**：ReleaseWorkflow 首次合入 main 之前，无法在 GitHub UI/API 手动触发它（`gh workflow run` 会 404）——这是平台限制。但发布流程依赖 `push` 事件自动触发，**不受此限制**。

---

## 4. 实测验证（fork 环境，2026-08-19）

在 `abcnx/MetaOpen` fork 上进行了完整实验（上游 ACANX/MetaOpen 零风险）：

| 事件 | 触发方式 | 结果 |
|------|---------|------|
| 第一次 push ReleaseWorkflow.yml 到 fork main | `push` 自动触发 | ✅ 自动执行，提取版本 0.8.8 → 创建了 tag V0.8.8 + Release |
| 第二次 push（pom.xml 版本改为 0.8.9） | `push` 自动触发 | ✅ 自动执行，创建了 tag V0.8.9 + Release |
| dry-run 验证（workflow_dispatch） | 手动触发 | ✅ 幂等保护正确跳过已存在 tag |

**关键证据**：fork 的 Actions run 历史显示，两次 push 事件均产生了 `event=push` 的 ReleaseWorkflow run（run 32210439746、32210518237），证明**首次推送 workflow 文件即触发**。

---

## 5. 边界情况与注意事项

### 5.1 幂等保护

ReleaseWorkflow 内置 `CheckIfTagExists` 步骤：若 `V<revision>` tag 已存在则跳过创建。因此即使因某种原因重复触发（如同一版本多次合并），也不会报错或覆盖已有 tag。

### 5.2 若确需手动触发（workflow 已合入 main 后）

```bash
# dry-run 预验证
gh workflow run ReleaseWorkflow.yml --repo ACANX/MetaOpen --ref main -f dry_run=true

# 正式触发
gh workflow run ReleaseWorkflow.yml --repo ACANX/MetaOpen --ref main
```

### 5.3 手动兜底（workflow 异常时）

```bash
git tag -a V0.8.9 -m "Release V0.8.9"
git push origin V0.8.9
gh release create V0.8.9 --generate-notes --repo ACANX/MetaOpen
```

---

## 6. 结论与建议

1. **发布流程无需等待**：第一次 dev→main 合并即可自动打 tag + 创建 Release
2. **首次合并前无法手动触发**（workflow_dispatch 平台限制），属正常现象，不影响自动流程
3. **建议**：正式发布时，dev→main 合并后到 Actions 页面确认 ReleaseWorkflow 已触发；若异常，使用第 5.3 节手动兜底

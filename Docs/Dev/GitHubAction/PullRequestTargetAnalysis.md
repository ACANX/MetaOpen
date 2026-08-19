# pull_request 与 pull_request_target 事件分析（PullRequestTargetAnalysis）

> 适用范围：MetaOpen 仓库 `.github/workflows/` 下所有 GitHub Actions 工作流的 PR 触发事件设计。
> 背景：`SonarCloudCodeAnalysis` 工作流因 fork PR 拿不到 secrets 持续失败（issue #2507），通过切换 `pull_request_target` 解决。本文档系统说明两种事件的机制差异、安全风险与最佳实践。
> 最后更新：2026-08-19

---

## 1. 两种事件的核心区别

| 维度 | `pull_request` | `pull_request_target` |
|------|----------------|-----------------------|
| 运行上下文 | PR 的合并提交（head 仓库） | **base 仓库**（目标分支所在仓库） |
| secrets 可用性 | fork PR ❌ 不传 / 同仓库 PR ✅ | ✅ **一律可用** |
| 工作流文件来源 | PR head（merge commit，**可被 PR 修改**） | **base 分支**（PR 改不了） |
| 默认 checkout 的代码 | PR 合并提交 | ⚠️ **base 分支代码**（不是 PR 代码） |
| `GITHUB_SHA` | PR merge commit 的 SHA | base 分支最新提交的 SHA |
| `GITHUB_REF` | `refs/pull/<N>/merge` | 目标分支 ref（如 `refs/heads/dev`） |
| `GITHUB_TOKEN` 权限（fork PR） | 只读 | 按工作流 `permissions:` 声明（可写） |
| fork PR 支持 | ❌（无 secrets 无法分析/上报） | ✅ |
| 安全性 | 安全（无 secrets 环境） | ⚠️ 有 secrets，需防不可信代码 |

## 2. 运行机制详解

### 2.1 `pull_request`：在"模拟合并"上运行

- PR 活动（opened / synchronize / reopened）时，GitHub 生成 head 与 base 的**模拟合并提交**（merge commit），工作流在该提交上运行。
- **工作流文件取自该 merge commit** —— 即 PR 内容可以修改工作流文件本身。对不传 secrets 的 fork PR 来说问题不大（没有秘密可窃取），但**同仓库 PR 也能改自己的工作流**（同仓库 PR 有 secrets，需注意审查）。
- fork PR 场景：secrets 一律不传（GitHub 安全设计，防 fork 窃取），`SONAR_TOKEN` 等为空 → 分析类任务必然失败。

### 2.2 `pull_request_target`：在 base 仓库上下文中运行

- 工作流在 **base 分支的最新提交**上运行，**工作流文件取 base 分支版本** —— PR 无法通过修改 `.github/workflows/` 劫持执行逻辑（安全性提升）。
- 因此 secrets 对 fork PR 也可用（运行环境属于 base 仓库）。
- **关键陷阱：默认 checkout 出来的是 base 分支代码**，不是 PR 的代码！要分析 PR 内容，必须显式检出 PR 合并提交：

  ```yaml
  - name: Checkout code
    uses: actions/checkout@v5
    with:
      fetch-depth: 0
      ref: refs/pull/${{ github.event.pull_request.number }}/merge
  ```

### 2.3 `refs/pull/<N>/head` 与 `refs/pull/<N>/merge` 的区别

| ref | 内容 | 适用 |
|-----|------|------|
| `refs/pull/<N>/head` | PR 分支最新提交（不含 base 后续改动） | 只看 PR 分支本身 |
| `refs/pull/<N>/merge` | PR 合并进 base 后的模拟合并提交（含 base 最新） | **推荐**（更接近合并后的最终状态，SonarCloud 官方示例采用） |

## 3. secrets 传递规则（GitHub 官方安全设计）

| 事件 × PR 来源 | secrets | GITHUB_TOKEN |
|----------------|---------|--------------|
| `pull_request` × 同仓库 PR | ✅ 传 | 完整权限（按 `permissions:`） |
| `pull_request` × fork PR | ❌ **不传**（空字符串） | 只读 |
| `pull_request_target` × 任意 PR | ✅ 传 | 完整权限（按 `permissions:`） |

- fork PR 的 `pull_request` 事件**永远拿不到 secrets**，与 fork 是否属于可信协作者无关。
- 若工作流需要 secrets 且要覆盖 fork PR，只能选择 `pull_request_target`（或改用仓库内分支发 PR）。

## 4. 安全风险与缓解（重要）

### 风险本质

`pull_request_target` 让 **PR 的代码在带 secrets 的环境中执行**。恶意 PR 可以在构建脚本（如 pom.xml 插件、Makefile、package.json scripts）中窃取环境变量里的 secrets。

### 缓解措施

1. **仅限可信协作者**：本仓库 PR 只来自 `abcnx`（自有账户），风险可控；若将来开放外部贡献者 PR，需重新评估
2. **权限最小化**：secrets 只给任务必需的最小权限（如 `SONAR_TOKEN` 仅 Execute Analysis）；`permissions:` 显式声明最小权限（本工作流 `contents: read`）
3. **高权限 secrets 禁止进入 PR 流程**：`OSSRH_TOKEN`、`GPG_PRIVATE_KEY` 等发布凭据绝不能出现在 `pull_request_target` 工作流中（只能用于 push 触发的发布流程）
4. **固定工作流逻辑**：工作流文件取 base 分支版本，步骤保持固定；不执行 PR 中引入的不可信脚本（如不 `npm install` PR 修改的依赖、不 `curl | sh` PR 提供的脚本）
5. **审查 PR 变更**：即使来自可信协作者，也审查 workflow 相关与构建脚本变更

### 什么时候用

- **默认优先 `pull_request`**：不需要 secrets 的任务（lint、编译、单元测试、静态检查）
- **需要 secrets 且面向 fork PR** 才用 `pull_request_target`：质量分析上报（SonarCloud）、覆盖率上报、部署预览等

## 5. SonarCloud 官方实践

SonarCloud 官方 GitHub Actions 示例（fork PR 场景）即采用 `pull_request_target`：

```yaml
name: SonarCloud
on:
  push:
    branches: [main]
  pull_request_target:
    types: [opened, synchronize, reopened]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
          ref: refs/pull/${{ github.event.pull_request.number }}/merge
      - uses: actions/setup-java@v4
        with:
          java-version: 17
          distribution: 'temurin'
      - name: Build and analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
```

要点：`pull_request` 事件下扫描器从 GitHub Actions 环境（`GITHUB_EVENT_NAME` / `GITHUB_REF`）自动识别 PR 上下文，**无需手动传参**。

> ⚠️ **重要限制（2026-08-19 实测）**：扫描器（engine 13.7）**仅对 `pull_request` 事件自动识别 PR 上下文**（依赖 `GITHUB_REF=refs/pull/N/merge`）。对 `pull_request_target` 事件（`GITHUB_REF` = base 分支 ref）**无法自动识别**，分析会被挂到 main 分支。必须**显式传入** `-Dsonar.pullRequest.key / -Dsonar.pullRequest.branch / -Dsonar.pullRequest.base`。详见第 7 节陷阱 #3。

## 6. MetaOpen 落地记录（SonarCloudCodeAnalysis.yml）

背景：issue #2507 —— SonarCloudCodeAnalysis 在 fork PR 上持续 401（`SONAR_TOKEN` 为空）。

| 变更 | 说明 |
|------|------|
| `pull_request` → `pull_request_target` | fork PR 获得 secrets，PR 分析恢复 |
| checkout 增加条件 ref | 仅 `pull_request_target` 事件检出 `refs/pull/<N>/merge`，push/workflow_dispatch 走事件默认 ref（空字符串 → checkout 自动用事件 SHA） |
| checkout 增加 `allow-unsafe-pr-checkout: true` | checkout v5 安全护栏：`pull_request_target` 默认拒绝检出 fork PR 代码，需显式放行（本仓库 PR 仅来自可信协作者 abcnx） |
| 移除 `-Dsonar.branch.name` | 该参数强制"分支模式"并跳过扫描器自动识别，导致 PR 分析被误当作名为 `<N>/merge` 的分支分析；移除后 push 事件由扫描器自动识别分支 |
| PR 事件显式传 `sonar.pullRequest.*` | 实测扫描器不识别 `pull_request_target` 事件的 PR 上下文（分析落 main 分支），需显式传入 key/branch/base；push 事件保持自动分支识别 |

参考 PR：#2514（qualitygate.wait 修复）、#2515（pull_request_target 切换）、#2517（JaCoCo 覆盖率 + allow-unsafe-pr-checkout）、#2519（显式 PR 上下文参数）。

## 7. 常见陷阱

| # | 陷阱 | 后果 | 规避 |
|---|------|------|------|
| 1 | `pull_request_target` 忘记 checkout merge ref | 扫描/构建的是 **base 分支**代码，PR 分析无效 | 显式 `ref: refs/pull/<N>/merge` |
| 2 | 同时声明 `pull_request` + `pull_request_target` | 每个 PR 触发**双份 run**（浪费 CI 分钟；同名 check 一红一绿干扰 required checks） | 只保留一种；`pull_request_target` 通吃两种 PR |
| 3 | 假设 `pull_request_target` 下扫描器能自动识别 PR | **实测不识别**：`pull_request_target` 的 `GITHUB_REF` 是 base 分支，扫描器（engine 13.7）只对 `pull_request` 事件自动识别 PR，分析被挂到 main 分支（PR 无装饰/检查） | `pull_request_target` 事件显式传 `-Dsonar.pullRequest.key/branch/base` |
| 4 | 触发方式切换的"鸡生蛋" | 切换 PR 自身不会用新触发方式运行（head 声明新事件、base 还是旧事件 → 都不匹配），须等合并后生效 | 合并后验证；无需特殊处理 |
| 5 | 混淆 `refs/pull/<N>/head` 与 `/merge` | head 不含 base 最新改动，结果偏离合并后状态 | 分析场景用 `/merge` |
| 6 | `pull_request_target` 里误用高权限 secrets | PR 代码可窃取发布凭据 | 发布凭据只进 push 流程 |

## 8. 决策指南与检查清单

### 决策指南

```
需要 secrets 吗？
├── 不需要 → 用 pull_request（默认，最安全）
└── 需要 →
    ├── 只服务同仓库 PR（dependabot 等）→ 用 pull_request 即可
    └── 需要覆盖 fork PR → 用 pull_request_target（并显式 checkout merge ref）
```

### 检查清单

- [ ] 明确该工作流是否需要 secrets、是否需覆盖 fork PR
- [ ] `pull_request_target` 场景已显式 checkout `refs/pull/<N>/merge`
- [ ] 未同时声明 `pull_request` 与 `pull_request_target`
- [ ] `permissions:` 已最小化（如 `contents: read`）
- [ ] 无高权限 secrets（发布凭据）出现在 PR 流程
- [ ] 扫描类任务未手动传 `sonar.branch.name` / `sonar.pullRequest.key`（交给自动识别）

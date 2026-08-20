# pull_request 与 pull_request_target 事件分析（PullRequestTargetAnalysis）

> 适用范围：MetaOpen 仓库 `.github/workflows/` 下所有 GitHub Actions 工作流的 PR 触发事件设计。
> 背景：`SonarCloudCodeAnalysis` 工作流需要让 SonarCloud 原生回写 `sonarqubecloud[bot]` 的 PR 评论。实践表明，这要求 PR 分析运行在 `pull_request` 上；`pull_request_target` 会把这类分析退化成分支分析，导致没有 PR 评论。
> 最后更新：2026-08-20

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

### 2.2 `pull_request_target`：不适合 SonarCloud 原生 PR 评论

- 工作流在 **base 分支的最新提交**上运行，**工作流文件取 base 分支版本**。
- 这对安全性有利，但 SonarCloud 在这里经常把分析落到 `branch=main` 之类的分支分析，而不是 `pullRequest=<N>`。
- 结果就是：GitHub check 可能有，`sonarqubecloud[bot]` 的 PR 评论却不会出现。
- 要拿到原生 PR 评论，优先使用 `pull_request` 事件，让 SonarCloud 自动识别 PR 上下文。

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
- 若工作流需要 secrets 且要覆盖 fork PR，可以选择 `pull_request_target`，但通常拿不到 SonarCloud 原生 PR 评论。

## 4. 安全风险与缓解（重要）

### 风险本质

`pull_request_target` 让 **PR 的代码在带 secrets 的环境中执行**。恶意 PR 可以在构建脚本（如 pom.xml 插件、Makefile、package.json scripts）中窃取环境变量里的 secrets。

### 缓解措施

1. **仅限可信协作者**：本仓库 PR 只来自 `abcnx`（自有账户），风险可控；若将来开放外部贡献者 PR，需重新评估
2. **权限最小化**：secrets 只给任务必需的最小权限（如 `SONAR_TOKEN` 仅 Execute Analysis）；`permissions:` 显式声明最小权限（本工作流为 `contents: read`、`pull-requests: read`）
3. **高权限 secrets 禁止进入 PR 流程**：`OSSRH_TOKEN`、`GPG_PRIVATE_KEY` 等发布凭据绝不能出现在 `pull_request_target` 工作流中（只能用于 push 触发的发布流程）
4. **固定工作流逻辑**：工作流文件取 base 分支版本，步骤保持固定；不执行 PR 中引入的不可信脚本（如不 `npm install` PR 修改的依赖、不 `curl | sh` PR 提供的脚本）
5. **审查 PR 变更**：即使来自可信协作者，也审查 workflow 相关与构建脚本变更

### 什么时候用

- **默认优先 `pull_request`**：需要 SonarCloud 原生 PR 评论的任务
- **需要 secrets 且面向 fork PR** 才考虑 `pull_request_target`：但这类任务通常不会得到 `sonarqubecloud[bot]` 的原生评论

## 5. SonarCloud 原生 PR 评论实践

要获得 SonarCloud 原生 PR 评论，工作流应使用 `pull_request`，让扫描器从 GitHub 的 PR 环境自动识别 PR：

```yaml
name: SonarCloud
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
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

要点：`pull_request` 事件下扫描器从 GitHub Actions 环境（`GITHUB_EVENT_NAME` / `GITHUB_REF`）自动识别 PR 上下文，**无需手动传入** `sonar.pullrequest.key/branch/base`。一次成功的 PR analysis 会同时产生 SonarCloud PR 记录、GitHub Check 和 PR decoration 评论。citeturn3search0turn0search1turn0search2

## 6. MetaOpen 落地记录（SonarCloudCodeAnalysis.yml）

背景：issue #2507 —— SonarCloudCodeAnalysis 在 fork PR 上持续 401（`SONAR_TOKEN` 为空）。当前工作流优先保证同仓库、目标 `main` 的 PR 获得 SonarCloud 原生 PR decoration；Free plan 不对目标为 `dev` 的 PR 提供 PR analysis。citeturn3search0

| 变更 | 说明 |
|------|------|
| `pull_request_target` → `pull_request` | 让 SonarCloud 自动识别 PR 上下文并回写 `sonarqubecloud[bot]` 评论 |
| 移除显式 `sonar.pullRequest.*` | `pull_request` 事件下由扫描器自动识别 PR 上下文，避免被当成分支分析 |
| 移除自定义 PR 回写脚本 | 由 SonarCloud 原生 bot 生成 Quality Gate / Issues 评论，避免重复维护一套评论逻辑 |

参考 PR：#2093（原生 SonarCloud bot 评论）、#2554（目标 `main` 的 PR）、#2558（目标 `dev` 的修复 PR）。

### 6.1 原生 PR 评论

目标是让 SonarCloud 自己生成像 `PR 2093` 那样的 `sonarqubecloud[bot]` 评论。它包含：

- Quality Gate passed / failed
- New issues / Accepted issues
- Security Hotspots
- Coverage / Duplication on New Code

如果分析跑进了 `branch=main`，就只会得到 GitHub check 或分支摘要，不会有这个 bot 评论。

## 7. 常见陷阱

| # | 陷阱 | 后果 | 规避 |
|---|------|------|------|
| 1 | `pull_request` 忘记 checkout merge ref | 扫描/构建的不是 PR 合并结果，PR 分析结果可能偏离实际合并状态 | 显式 `ref: refs/pull/<N>/merge` |
| 2 | 同时声明 `pull_request` + `pull_request_target` | 每个 PR 触发**双份 run**（浪费 CI 分钟；同名 check 一红一绿干扰 required checks） | 只保留一种；`pull_request_target` 通吃两种 PR |
| 3 | 假设 `pull_request_target` 下扫描器能自动识别 PR | **实测不识别**：`pull_request_target` 的 `GITHUB_REF` 是 base 分支，分析容易挂到 `branch=main`，PR 不会出现 `sonarqubecloud[bot]` 评论 | 改用 `pull_request` 事件让 SonarCloud 自动识别 PR |
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
- [ ] `pull_request` 场景已显式 checkout `refs/pull/<N>/merge`
- [ ] 未声明 `pull_request_target` 作为 SonarCloud PR 分析入口
- [ ] SonarCloud 分析运行在 `pull_request` 事件上
- [ ] PR 评论由 `sonarqubecloud[bot]` 自动生成
- [ ] `permissions:` 已最小化（`contents: read`、`pull-requests: read`）
- [ ] 无高权限 secrets（发布凭据）出现在 PR 流程
- [ ] push 事件仍可正常生成分支分析

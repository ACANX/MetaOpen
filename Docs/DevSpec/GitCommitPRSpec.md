# Git 提交与 Pull Request 规范（GitCommitPRSpec）

> 适用范围：MetaOpen 仓库所有 Git 提交（commit message）、分支管理与 Pull Request。
> 最后更新：2026-08-19

---

## 1. 提交信息规范（Conventional Commit）

提交信息采用 **Conventional Commit 风格**，格式：`<type>: <描述>`（`<scope>` 可选）。

| type | 用途 | 示例 |
|------|------|------|
| `feat:` | 新功能 | `feat: 新增行情快照接口` |
| `fix:` | Bug 修复 | `fix: 修复并发下单竞态问题` |
| `docs:` | 文档更新 | `docs: 补充 DevSpec 文档命名规范` |
| `test:` | 测试相关 | `test: 补充 QuoteService 单元测试` |
| `refactor:` | 代码重构 | `refactor: 抽取公共校验逻辑` |
| `build:` | 构建系统/依赖 | `build: 升级 maven-compiler-plugin` |
| `chore:` | 其他杂项 | `chore(deps): 升级 httpclient5 5.6.2 → 5.6.4` |

### 提交要求

- **依赖升级提交必须明确写出构件名和版本变化**（`构件 旧版本 → 新版本`），如 `chore(deps): 升级 org.json:json 20260522 → 20260701`。
- 提交描述使用简体中文，简洁概括变更内容。
- 一个提交只做一件事：功能、修复、文档、依赖升级分开提交。
- 禁止提交密钥、签名文件、Maven Central 凭据、本地 IDE 配置等敏感/本地文件。

## 2. 分支命名规范

| 分支类型 | 命名 | 示例 |
|----------|------|------|
| 新功能 | `feat/<描述>` | `feat/backtest-engine` |
| Bug 修复 | `fix/<描述>` | `fix/quote-timeout` |
| 文档 | `docs/<描述>` | `docs/devspec-supplement` |
| 重构 | `refactor/<描述>` | `refactor/exception-handler` |
| Issue 处理 | `issue-<编号>-<描述>` | `issue-2507-sonar-token` |

## 3. Pull Request 要求

PR 描述应包含：

1. **变更内容**：本次改了什么，为什么改。
2. **受影响模块**：列出涉及的模块（如 `meta-model/model-rss`、`.github/workflows/`）。
3. **关联 issue**：如有关联 Issue，使用 `Closes #<编号>` 自动关闭。
4. **测试依据**：提供 `mvn test` 或目标模块测试命令输出；涉及依赖/行为变更需说明验证方式。
5. **截图**：只有文档或工作流界面变更需要截图，代码变更不强制。

## 4. Git 工作流红线（必须遵守）

- **禁止**直接推送代码到 `dev`、`main` 等受保护分支。
- **必须**通过 PR 流程递交：
  1. 创建特性分支（`feat/`、`fix/`、`docs/`、`refactor/` 等）
  2. 在特性分支上提交代码
  3. 推送到个人 Fork 远端仓库
  4. 以个人 Fork 的特性分支为来源，向上游仓库发起正式 PR（base 为目标分支，如 `dev`）
  5. 等待 PR 审核和合并；合并后清理本地和远端特性分支
- **PR 合并前检查**：用 `gh pr view <编号> --json state,mergedAt` 确认目标 PR 状态；**已合并的 PR 不得追加代码**，需新建分支和 PR。
- 推送后**确认 PR 状态与 CI 结果**（如 `MultiMavenJDKBranchCI` 是否通过）。
- **例外**：仅当仓库所有者（ACANX）明确授权"直接推送"时才可跳过 PR 流程。
- **违反后果**：首次违反严厉批评并检讨；再次违反终身禁用 Git 操作权限。

## 5. 检查清单

- [ ] 提交信息为 Conventional Commit 风格（`type:` 前缀）
- [ ] 依赖升级提交写明构件名和版本变化
- [ ] 分支命名符合规范（`feat/`、`fix/`、`docs/` 等）
- [ ] 未直接推送受保护分支（`dev`、`main`）
- [ ] PR 描述含变更内容、受影响模块、测试依据
- [ ] 已检查目标 PR 状态（未合并、可追加）
- [ ] 推送后已确认 PR 状态与 CI 结果

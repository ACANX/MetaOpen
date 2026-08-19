# 开发规范（DevSpec）

> 适用范围：MetaOpen 仓库所有开发相关规范。每项规范独立成文，存放于 `Docs/DevSpec/` 目录下。
> 最后更新：2026-08-19

---

## 目录定位

`Docs/DevSpec/` 是 MetaOpen 仓库的**开发规范目录**，沉淀仓库所有者（ACANX）口述确定的开发约定，包括命名规范、编码风格、提交规范、测试规范、版本管理约定等。

与 [AGENTS.md](../../AGENTS.md) 的关系：

- **AGENTS.md**：仓库级指南（速查），汇总各规范的核心要点，供开发者（含 AI Agent）进仓后快速了解约定。
- **Docs/DevSpec/**：各规范的详细正文，含正确/错误示例与检查清单。
- 两者内容保持一致：规范正文以 `Docs/DevSpec/` 为准，AGENTS.md 保留摘要并指向本文档。

## 规范文档索引

| 文档 | 内容 | 适用场景 |
|------|------|---------|
| [GitHubActionWorkflowSpec.md](./GitHubActionWorkflowSpec.md) | GitHub Action 工作流编写规范（文件/name/step 命名、参考示例） | 新建或修改 `.github/workflows/` 下的工作流 |
| [DocumentNamingSpec.md](./DocumentNamingSpec.md) | Markdown 文档文件命名规范（大驼峰、无连字符） | 创建/重命名 `Docs/` 及仓库内文档 |
| [CodeStyleSpec.md](./CodeStyleSpec.md) | Java 编码风格与命名约定（缩进、包名、标识符风格） | 编写/审查 Java 代码 |
| [ModuleNamingSpec.md](./ModuleNamingSpec.md) | Maven 模块命名约定（model-*/sdk-*/api-*/base-*） | 新建模块、调整模块结构 |
| [GitCommitPRSpec.md](./GitCommitPRSpec.md) | Git 提交信息与 Pull Request 规范（Conventional Commit、PR 红线） | 提交代码、发起 PR |
| [TestSpec.md](./TestSpec.md) | 测试规范（JUnit Jupiter、*Test 命名、运行方式） | 编写/运行单元测试 |
| [VersionReleaseSpec.md](./VersionReleaseSpec.md) | 版本管理与发布约定（revision 单一事实源、Tag、ReleaseWorkflow） | 版本号修改、正式发版 |

## 通用约定

- **交流语言**：与用户对话、文档、注释、提交信息、PR 描述默认使用**简体中文**；代码标识符（类名、方法名、变量名）使用英文。
- **文档命名**：本目录及仓库内所有 Markdown 文档文件名遵循大驼峰命名（见 [DocumentNamingSpec.md](./DocumentNamingSpec.md)），本 README 的索引表格需随规范文档的新增/删除同步更新。
- **规范来源**：本目录内容来源于仓库所有者的口述确定与项目实践沉淀；对规范有疑问或需要新增规范时，先与仓库所有者确认，再按"新增规范流程"落地。

## 新增规范流程

1. 按 [DocumentNamingSpec.md](./DocumentNamingSpec.md) 创建 `<主题>Spec.md` 文档（参考既有规范的章节结构：适用范围 → 规范正文 → 正确/错误示例 → 检查清单）。
2. 更新本 `README.md` 的规范文档索引表。
3. 同步更新 [AGENTS.md](../../AGENTS.md) 对应摘要章节（或新增摘要章节），并指向新规范文档。
4. 更新 [Docs/README.md](../README.md) 的 DevSpec 目录表格。
5. 通过 PR（base=`dev`）递交，遵循 [GitCommitPRSpec.md](./GitCommitPRSpec.md) 的 PR 红线。

## 检查清单

- [ ] 新规范文件名大驼峰、`.md` 扩展名
- [ ] 本 README 索引表已同步
- [ ] AGENTS.md 摘要已同步
- [ ] Docs/README.md 目录表格已同步
- [ ] 已通过 PR 递交（base=`dev`）

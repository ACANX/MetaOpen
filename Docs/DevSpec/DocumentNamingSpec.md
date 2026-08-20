# 文档文件命名规范（DocumentNamingSpec）

> 适用范围：MetaOpen 仓库 `Docs/` 目录及仓库内所有 Markdown 文档文件（`.md`）。
> 最后更新：2026-08-19

---

## 1. 文件命名规范

- **Markdown 文档文件名使用大驼峰（PascalCase）命名**，不使用连字符（`-`）、下划线（`_`）或空格。
- 正确示例：
  - `PreReleaseChecklist.md`
  - `PostReleaseChecklist.md`
  - `WorkflowTriggerAnalysis.md`
  - `BomDependencyAnalysis.md`
  - `GitHubActionWorkflowSpec.md`
- 错误示例：
  - `PreRelease-Checklist.md` ❌（含连字符）
  - `workflow_trigger.md` ❌（含下划线 + 非大驼峰）
  - `my doc.md` ❌（含空格）
  - `release-checklist.md` ❌（小写 + 连字符）
- 文件名应能概括文档主题，语义清晰。

## 2. 命名风格一致性

- 文档文件命名风格与 GitHub Action 工作流文件命名风格保持一致（均为大驼峰、无分隔符），见 [GitHubActionWorkflowSpec.md](./GitHubActionWorkflowSpec.md) 第 1 节。
- 目录名不强制大驼峰（如 `Docs/Dev/Introduction/`、`Docs/DevSpec/`），但**文件名必须大驼峰**。

## 3. 重命名要求

- 重命名文档时，需**同步更新所有引用该文件的链接**，包括：
  - 其他 Markdown 文档中的相对链接（如 `[xxx](./DevSpec/xxx.md)`）
  - 根 `README.md`、`Docs/README.md`、`AGENTS.md` 中的索引与引用
- 避免出现指向旧文件名的失效链接。

## 4. 检查清单

- [ ] 文件名大驼峰（PascalCase）、`.md` 扩展名
- [ ] 无连字符、下划线、空格
- [ ] 文件名语义清晰，能概括文档主题
- [ ] 新增/重命名后，所有引用链接已同步更新

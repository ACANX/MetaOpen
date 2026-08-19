# 版本管理与发布约定（VersionReleaseSpec）

> 适用范围：MetaOpen 仓库版本号管理、Tag 创建与正式发布（Release）流程。
> 最后更新：2026-08-19

---

## 1. 版本单一事实源

- 根 `pom.xml` 的 **`<revision>` 属性**是版本号的唯一事实来源，子模块通过 `${revision}` 继承，全仓库版本号统一。
- 版本号格式：`0.x.y`（如 `0.8.9`），对应 Tag 为 `V0.8.9`。

## 2. 发版改号方式

- **版本号变更统一走 `UpdateProjectVersion` workflow**（`.github/workflows/` 下，手动触发）。
- 该 workflow 会同步更新：
  - 根 `pom.xml` 的 `<revision>`
  - `meta-open.version`、`meta.version` 等版本属性
  - `bom-graalvm.version`（派生值 `25.<revision>`，如 `25.0.8.9`）
- **禁止手动修改版本号**：`bom-graalvm.version` 由发版流程程序化派生替换，手动修改会导致版本断层（下次发版被自动覆盖、或与 BOM 依赖分析结果不一致）。

## 3. Tag 与 Release 创建

- **Tag 命名**：`V<version>`（如 `V0.8.9`），与版本号保持一致。
- **ReleaseWorkflow**（`.github/workflows/ReleaseWorkflow.yml`）在 **dev → main 合并后自动触发**：
  1. 从根 `pom.xml` 提取 `<revision>` 版本号
  2. 检查对应 Tag 是否已存在（已存在则跳过，保证幂等）
  3. 创建并推送 Tag `V<version>`
  4. 创建 GitHub Release（`generate_release_notes: true` 自动生成发布说明）
- 支持 `dry_run=true` 手动触发**预验证**：仅校验逻辑，不实际创建 Tag 和 Release。

## 4. 发布流程

1. **发版前**：按 [Docs/Release/PreReleaseChecklist.md](../Release/PreReleaseChecklist.md) 逐项检查（版本号、阻断事项、CI 状态等）。
2. **发起发布**：dev → main 发起正式发版 PR（标题如 `Release: V0.8.9`），合并后自动触发 ReleaseWorkflow 打 Tag + 建 Release。
3. **发版后**：按 [Docs/Release/PostReleaseChecklist.md](../Release/PostReleaseChecklist.md) 收尾（核对 Tag/Release、可选 Maven Central 发布、清理分支等）。

## 5. 注意事项

- **首次合并触发问题**：ReleaseWorkflow 首次随 dev→main 合并进入 main 分支时，该次合并本身不会触发（触发只对合并**之后**的 push 生效），详见 [Docs/Release/WorkflowTriggerAnalysis.md](../Release/WorkflowTriggerAnalysis.md) 探究。
- 修改发布相关配置（GPG、source、Javadoc、flatten、Central publishing 插件）时应谨慎，并同步检查相关 GitHub Actions 工作流。

## 6. 检查清单

- [ ] 版本号只通过 `UpdateProjectVersion` workflow 修改，未手动改 `pom.xml`
- [ ] Tag 命名 `V<version>` 与版本号一致
- [ ] 发版前已按 PreReleaseChecklist 检查
- [ ] 发版后已按 PostReleaseChecklist 收尾
- [ ] 核心 workflow 修改遵循 [GitHubActionWorkflowSpec.md](./GitHubActionWorkflowSpec.md)

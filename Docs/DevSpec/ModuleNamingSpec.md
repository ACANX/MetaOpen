# Maven 模块命名约定（ModuleNamingSpec）

> 适用范围：MetaOpen 仓库内所有 Maven 模块（根 `pom.xml` 聚合的子模块）。
> 最后更新：2026-08-19

---

## 1. 模块命名模式

模块命名遵循现有模式，按模块职责使用统一前缀，**禁止随意命名新模块**。

| 模块类型 | 命名前缀 | groupId | 示例 |
|----------|----------|---------|------|
| 领域模型模块 | `model-*` | `com.acanx.meta.model` | `model-rss`、`model-gemini`、`model-quote`、`model-deepseek` |
| 组件 / SDK 模块 | `sdk-*` / `api-*` | `com.acanx.meta.component` | `sdk-maven-artifact` |
| 基础能力模块 | `base-*` | `com.acanx.meta.base` | `base-exception` |

- 正确示例：
  - `model-rss`、`model-gemini`（领域模型）
  - `base-exception`（基础能力）
  - `sdk-maven-artifact`（Maven 构件相关组件）
- 错误示例：
  - `rss-model-x` ❌（前缀位置混乱）
  - `common-utils` ❌（未按 `base-*` / `model-*` 前缀归类）

## 2. 新建模块要求

- 新模块建议使用 `tool-archetype` 生成，具体步骤见 [Docs/Dev/Introduction/NewArtifactGuide.md](../Dev/Introduction/NewArtifactGuide.md)。
- 新模块必须加入对应父 POM 的 `<modules>` 列表（`base`、`meta-model`、`meta-component`、`meta-bom`、`meta-sdk`、`os-dependencies` 中对应的聚合 POM）。
- 模块命名需在仓库内全局唯一，且与模块内 `artifactId` 一致（不额外加后缀）。

## 3. 模块归属

| 聚合模块 | 收录范围 |
|----------|----------|
| `base/*` | 通用基础能力模块（`base-*`） |
| `meta-model/model-*` | 领域模型模块（`model-*`） |
| `meta-component/sdk-*` | Maven 构件相关组件 / SDK 模块（`sdk-*`、`api-*`） |

> 完整模块组织说明见根 `pom.xml` 与 [AGENTS.md](../../AGENTS.md) 的"项目结构与模块组织"章节。

## 4. 检查清单

- [ ] 模块名使用 `model-*` / `sdk-*` / `api-*` / `base-*` 前缀
- [ ] groupId 与模块类型匹配（model / component / base）
- [ ] 已加入对应父 POM 的 `<modules>`
- [ ] 模块名全局唯一

# 增量发布（Incremental Deploy）

> 说明：本文档整理自原 `Docs/增量发布.md`，补充了说明与注意事项。

## 概述

增量发布指**只构建并部署发生变更的模块**（及其依赖），而不是全量构建整个项目，可显著缩短发布耗时、减少不必要的制品发布。

## 基本用法

使用 Maven 的 `-pl`（project list）参数指定要发布的模块：

```bash
# 部署指定模块（逗号分隔多个模块）
mvn deploy -pl module-a,common-lib

# 示例：发布 model-rss 和 model-gemini
mvn deploy -pl model-rss,model-gemini
```

## 常用组合参数

| 参数 | 作用 |
|------|------|
| `-pl <模块列表>` | 指定要操作的模块（逗号分隔） |
| `-am`（--also-make） | 同时构建所选模块的**依赖模块**（上游依赖） |
| `-amd`（--also-make-dependents） | 同时构建**依赖所选模块**的模块（下游） |
| `-B` | 批处理模式（非交互） |
| `-U` | 强制更新远程 snapshot 依赖 |
| `-DskipTests` / `-Dmaven.test.skip=true` | 跳过测试 |

## 实用示例

### 发布某个模块及其依赖

```bash
mvn deploy -pl model-rss -am
```

### 发布多个模块

```bash
mvn deploy -pl model-rss,model-gemini -am
```

### 仅验证不部署

```bash
mvn install -pl model-rss -am -DskipTests
```

## 注意事项

1. **依赖顺序**：`deploy -pl A` 时，A 依赖的模块必须已存在于本地 `.m2` 或远程仓库；若本地没有且不想全量构建，使用 `-am` 一并构建
2. **版本一致性**：模块间依赖使用 `${revision}` 等属性统一管理，增量发布前确认目标版本已在各模块中一致（参考 `Docs/Release/` 的版本管理说明）
3. **发布范围确认**：`-pl` 列表遗漏模块不会自动发布，需人工确认本次发布范围
4. **正式发版**：MetaOpen 正式发布通常走 `ReleaseFullArtifactsByBatch` workflow 全量发布；增量发布适用于日常迭代或局部修复后的快速发布

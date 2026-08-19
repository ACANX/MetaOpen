# BOM 依赖版本分析

> 说明：本文档整理自原 `Docs/BOM依赖版本分析.md`，补充了实际使用场景与说明。

## 概述

MetaOpen 使用 Maven BOM（Bill of Materials）统一管理依赖版本。`meta-bom` 下包含多个子 BOM（`bom-graalvm`、`bom-sdk`、`bom-deamon`、`bom-cf`、`bom-mod`、`bom-aio`），其中 `bom-aio` 是聚合展开后的"成品 BOM"，`bom-aio-origin` 是生成它的中间跳板模块。

## 分析命令

在需要分析某个模块实际生效的依赖版本（effective-pom）时，在对应模块目录执行：

```bash
# 在模块根目录生成 effective-pom.xml
mvn help:effective-pom
```

生成的文件为 `effective-pom.xml`，包含该模块**经过继承、依赖管理导入、属性展开后**的最终 POM 形态，是排查依赖版本冲突、确认 BOM 导入结果的主要依据。

## 常见使用场景

### 1. 分析 bom-aio 的展开结果

`bom-aio/pom.xml` 由 `UpdateBOMAIODeps` workflow 自动生成，其内容来自 `bom-aio-origin` 的 effective-pom 展开。如需手动分析：

```bash
cd meta-bom/bom-aio-origin
mvn help:effective-pom -Doutput=effective-pom.xml
```

生成后查看 `effective-pom.xml` 中 `dependencyManagement` 的依赖版本，即 bom-aio 最终对外提供的版本清单。

### 2. 排查某个依赖的最终版本

```bash
# 生成 effective-pom 后，搜索目标依赖
grep -A3 "<artifactId>目标依赖</artifactId>" effective-pom.xml
```

### 3. 验证 BOM 导入链

`bom-aio-origin` 通过 `<scope>import</scope>` 导入 `os-dependencies`、`bom-deamon`、`bom-mod`、`bom-sdk`、`bom-cf`、`bom-graalvm` 等多个 BOM，可用 effective-pom 验证导入后的版本覆盖关系（谁后导入谁生效，取决于 dependencyManagement 声明顺序）。

## 注意事项

- `effective-pom.xml` 是构建产物，不应提交到仓库（`.gitignore` 已排除或使用后删除）
- 分析时确保本地 `.m2` 仓库已包含所需版本的依赖（必要时先 `mvn install`）
- 若发现版本与预期不符，优先检查各 BOM 的导入顺序与属性定义

# 如何生成新的 Artifact（新模块创建指南）

> 说明：本文档整理自原 `Docs/README.md` 中的命令示例，补充了说明。

## 概述

MetaOpen 使用 `tool-archetype`（`com.acanx.java.tool`）Maven 原型快速生成新模块骨架。执行后会在当前目录下创建指定 `groupId` / `artifactId` 的 Maven 模块。

## 命令模板

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.acanx.java.tool \
  -DarchetypeArtifactId=tool-archetype \
  -DarchetypeVersion=<版本号> \
  -DgroupId=<目标 groupId> \
  -DartifactId=<目标 artifactId> \
  -Dversion=<初始版本>
```

## 历史命令参考

以下为项目历史中使用过的原型生成命令（`model-*` 领域模型模块、`sdk-*`/`api-*` 组件模块）：

```bash
mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.1.6 -DgroupId=com.acanx.meta.model -DartifactId=model-quote -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.1.6 -DgroupId=com.acanx.meta.model -DartifactId=model-sonatype -Dversion=0.1.2

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.model -DartifactId=model-wechat-work -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.model -DartifactId=model-dingtalk -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.model -DartifactId=autil-incubator -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.model -DartifactId=model-test -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.model -DartifactId=model-maven -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.component -DartifactId=sdk-115-open -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.component -DartifactId=sdk-maven-artifact -Dversion=0.1.0

mvn archetype:generate -DarchetypeGroupId=com.acanx.java.tool -DarchetypeArtifactId=tool-archetype -DarchetypeVersion=0.2.0 -DgroupId=com.acanx.meta.component -DartifactId=api-115-open -Dversion=0.1.0
```

## 新模块创建步骤

1. **确认命名**：模型类模块用 `model-*`（groupId `com.acanx.meta.model`）；组件模块用 `sdk-*` / `api-*`（groupId `com.acanx.meta.component`）
2. **执行原型命令**：按上述模板生成模块骨架
3. **加入聚合 POM**：将新模块添加到对应父模块的 `<modules>` 中（如 `meta-model/pom.xml`、`meta-component/pom.xml`）
4. **调整版本**：确认 `version` 与项目 `${revision}` 一致
5. **本地验证**：`mvn -pl <新模块> -am install` 验证构建通过

## 注意事项

- 原型版本（`tool-archetype` 的 `-DarchetypeVersion`）以当前可用版本为准，历史命令中的版本号可能已过时
- 生成后需检查并调整自动生成的 POM（依赖、插件、parent 引用等）
- 不要在生成目录外随意放置模块，遵循现有目录结构（`meta-model/model-*`、`meta-component/*`）

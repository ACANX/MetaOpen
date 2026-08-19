# MetaOpen

Meta Open Source Components —— 开源的 Java 基础组件库集合。

MetaOpen 是一套基于 **Java 21** 的 Maven 多模块开源组件，提供通用基础能力（base）、领域模型（meta-model）、云服务组件（meta-component）、SDK 封装（meta-sdk）以及统一的依赖管理 BOM（meta-bom），开箱即用、按需引入。

## 项目结构

```
MetaOpen
├── base/               # 通用基础能力（base-error、base-exception、base-file、base-http、base-page、base-rest）
├── meta-model/         # 领域模型模块（model-security、model-quote、model-sonatype、model-llm、model-rss 等）
├── meta-component/     # 云服务/组件模块（如 sdk-maven-artifact）
├── meta-sdk/           # SDK 封装（sdk-llm、sdk-maven-artifact 等）
├── meta-bom/           # 依赖管理 BOM（bom-graalvm、bom-sdk、bom-deamon、bom-cf、bom-mod、bom-aio）
├── os-dependencies/    # 系统级依赖版本统一管理
├── Docs/               # 项目文档（开发入门 / 发布方案）
└── pom.xml             # 根聚合 POM（版本号单一事实源：<revision>）
```

## 特性

- **Java 21** 多模块架构，按需引入、独立演进
- **统一版本管理**：根 POM 通过 `<revision>` 属性统一控制版本，子模块自动继承
- **BOM 依赖管理**：`meta-bom` 提供聚合 BOM（含 bom-aio 全量依赖清单），简化依赖版本维护
- **自动化发布**：GitHub Actions 支持发版改号、依赖同步、自动打 tag 与 Release（详见 [Docs/Release/README.md](Docs/Release/README.md)）

## 编译

```aiignore
mvn clean install -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED

# 编译时的VM参数
-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED

```

> 提示：若只需构建某个模块，可使用 `mvn -pl <module> -am install`（详见 [Docs/Dev/Introduction/IncrementalDeploy.md](Docs/Dev/Introduction/IncrementalDeploy.md)）。

## 文档

- **开发入门**：[Docs/Dev/Introduction/](Docs/Dev/Introduction/)（新模块创建、增量发布、BOM 依赖分析）
- **发布方案**：[Docs/Release/](Docs/Release/)（发布流程、检查清单）
- **开发规范**：[AGENTS.md](AGENTS.md)（语言、命名、提交规范等）

## Reference

- [Meta-Open Overview - Sonatype](https://central.sonatype.com/artifact/com.acanx.meta/meta-open/overview)
- [meta-open - MvnRepository](https://mvnrepository.com/artifact/com.acanx.meta/meta-open)
- [os-dependencies - MvnRepository](https://mvnrepository.com/artifact/com.acanx.meta/os-dependencies)

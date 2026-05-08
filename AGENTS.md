# 仓库指南

## 交流语言

与用户对话时优先使用简体中文。除非用户明确要求使用其他语言，说明、进度更新、总结和建议都应使用简体中文。

## 项目结构与模块组织

MetaOpen 是一个 Java 21 Maven 多模块项目。根目录 `pom.xml` 聚合 `base`、`meta-model`、`meta-component`、`meta-bom`、`meta-sdk` 和 `os-dependencies`。各模块使用标准 Maven 目录结构：`src/main/java`、`src/main/resources`、`src/test/java` 和 `src/test/resources`。通用基础能力位于 `base/*`；领域模型模块位于 `meta-model/model-*`；Maven 构件相关组件位于 `meta-component/sdk-maven-artifact`。项目文档在 `Docs/`，GitHub 自动化配置在 `.github/workflows/`。

## 构建、测试与开发命令

- `mvn test`：使用 Maven Surefire 运行所有模块的单元测试。
- `mvn -pl meta-model/model-rss test`：只测试指定模块。
- `mvn -pl meta-model/model-rss -am test`：测试指定模块，并同时构建其依赖模块。
- `mvn clean install -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8`：完整构建并安装所有构件到本地 Maven 仓库。

部分构建可能需要 `README.md` 中记录的 `--add-opens=jdk.compiler/...=ALL-UNNAMED` JVM 参数，尤其是涉及注解处理器或 JDK 编译器内部 API 时。

## 编码风格与命名约定

使用 UTF-8 和父 POM 中定义的 Java 21 配置。保持现有 Java 风格：4 空格缩进，包名位于 `com.acanx.meta` 下，类名使用 PascalCase，方法和字段使用 camelCase，常量使用全大写。模块命名遵循现有模式，例如 `model-rss`、`model-gemini` 和 `base-exception`。优先使用模块内的小型模型类和工具类，只有在复用价值明确时才抽象到共享层。

## 测试指南

测试框架为 JUnit Jupiter 6，测试运行器为 Maven Surefire。测试代码放在对应模块的 `src/test/java` 下；测试资源和 `meta/app.yaml` 或 `meta/app.yml` 放在 `src/test/resources` 下。测试类命名遵循现有 `*Test` 模式，例如 `ArtifactServiceTest` 或 `AppTest`。大范围修改前运行 `mvn test`；局部模块修改可运行 `mvn -pl <module> -am test`。

## 提交与 Pull Request 规范

近期提交历史采用 Conventional Commit 风格，尤其是 `chore(deps): ...`。提交信息建议使用简短前缀，例如 `feat:`、`fix:`、`docs:`、`test:` 或 `chore(deps):`。依赖升级提交应明确写出构件名和版本变化。Pull Request 应说明变更内容、列出受影响模块、关联相关 issue，并提供测试依据，例如 `mvn test` 或目标模块测试命令输出。只有文档或工作流界面变更需要截图。

## 安全与配置提示

不要提交密钥、签名文件、Maven Central 凭据或本地 IDE 配置。发布配置涉及 GPG、source、Javadoc、flatten 和 Central publishing 插件；修改发布相关配置时应谨慎，并同步检查相关 GitHub Actions 工作流。

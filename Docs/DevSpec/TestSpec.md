# 测试规范（TestSpec）

> 适用范围：MetaOpen 仓库内所有单元测试代码与测试运行方式。
> 最后更新：2026-08-19

---

## 1. 测试框架与运行器

- 测试框架：**JUnit Jupiter 6**（JUnit 5 平台）。
- 测试运行器：**Maven Surefire**（`mvn test`）。
- 测试代码必须放在对应模块的 `src/test/java` 下。

## 2. 测试资源

- 测试资源放在对应模块的 `src/test/resources` 下。
- 应用配置（`meta/app.yaml` 或 `meta/app.yml`）放在 `src/test/resources` 下，供测试环境加载。

## 3. 测试类命名

- 测试类命名遵循 `*Test` 模式。
- 正确示例：
  - `ArtifactServiceTest`
  - `AppTest`
  - `QuoteServiceTest`
- 错误示例：
  - `ArtifactServiceTests` ❌（复数形式不一致）
  - `testQuoteService` ❌（未使用 PascalCase + `*Test` 后缀）

## 4. 运行方式

| 场景 | 命令 |
|------|------|
| 全量单元测试 | `mvn test` |
| 只测试指定模块 | `mvn -pl meta-model/model-rss test` |
| 测试指定模块及其依赖模块 | `mvn -pl meta-model/model-rss -am test` |
| 完整构建并安装到本地仓库 | `mvn clean install -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8` |

- **大范围修改前**运行 `mvn test` 确认不破坏既有功能。
- **局部模块修改**可运行 `mvn -pl <module> -am test` 快速验证（`-am` 同时构建依赖模块，避免缺依赖失败）。
- 部分构建可能需要 `README.md` 中记录的 `--add-opens=jdk.compiler/...=ALL-UNNAMED` JVM 参数（涉及注解处理器或 JDK 编译器内部 API 时）。

## 5. 测试编写要求

- 测试应覆盖核心逻辑的**正常路径与边界/异常路径**。
- 测试方法命名建议：`<方法名>_<场景>_<期望>`（如 `getQuote_symbolNotFound_throwsException`），与主代码风格保持一致即可。
- 不提交依赖外部环境的测试（如需要真实网络/数据库的测试应做好 mock 或标注跳过条件）。

## 6. 检查清单

- [ ] 测试代码位于 `src/test/java`，测试资源位于 `src/test/resources`
- [ ] 测试类命名 `*Test`（PascalCase）
- [ ] 测试配置 `meta/app.yaml`（或 `meta/app.yml`）在 `src/test/resources`
- [ ] 大范围修改前已运行 `mvn test`
- [ ] 局部修改已运行 `mvn -pl <module> -am test` 验证

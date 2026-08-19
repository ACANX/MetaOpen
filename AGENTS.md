# 仓库指南（AGENTS.md）

本文件为 MetaOpen 仓库的开发规范与协作约定，供开发者（含 AI Agent）在仓库内工作时遵循。

## 交流语言

与用户对话时优先使用简体中文。除非用户明确要求使用其他语言，说明、进度更新、总结和建议都应使用简体中文。

## 项目结构与模块组织

MetaOpen 是一个 Java 21 Maven 多模块项目。根目录 `pom.xml` 聚合 `base`、`meta-model`、`meta-component`、`meta-bom`、`meta-sdk` 和 `os-dependencies`。各模块使用标准 Maven 目录结构：`src/main/java`、`src/main/resources`、`src/test/java` 和 `src/test/resources`。通用基础能力位于 `base/*`；领域模型模块位于 `meta-model/model-*`；Maven 构件相关组件位于 `meta-component/sdk-maven-artifact`。项目文档在 `Docs/`（开发入门 `Docs/Dev/Introduction/`、发布方案 `Docs/Release/`），GitHub 自动化配置在 `.github/workflows/`。

## 构建、测试与开发命令

- `mvn test`：使用 Maven Surefire 运行所有模块的单元测试。
- `mvn -pl meta-model/model-rss test`：只测试指定模块。
- `mvn -pl meta-model/model-rss -am test`：测试指定模块，并同时构建其依赖模块。
- `mvn clean install -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8`：完整构建并安装所有构件到本地 Maven 仓库。

部分构建可能需要 `README.md` 中记录的 `--add-opens=jdk.compiler/...=ALL-UNNAMED` JVM 参数，尤其是涉及注解处理器或 JDK 编译器内部 API 时。

## 编码风格与命名约定

使用 UTF-8 和父 POM 中定义的 Java 21 配置。保持现有 Java 风格：4 空格缩进，包名位于 `com.acanx.meta` 下，类名使用 PascalCase，方法和字段使用 camelCase，常量使用全大写。模块命名遵循现有模式，例如 `model-rss`、`model-gemini` 和 `base-exception`。优先使用模块内的小型模型类和工具类，只有在复用价值明确时才抽象到共享层。

### 代码质量红线（SonarCloud，强制）

SonarCloud 静态扫描是强制环节（push / PR 自动分析），新代码不得新增任何 issue，HIGH 影响问题必须当 PR 内解决。重点规则：

- **S1186 空方法/空构造器**：框架需要无参构造器时保留并在方法体内加嵌套注释说明；无其他构造器且无需保留时直接删除（编译器自动生成等价构造器）；禁止用 `throw new UnsupportedOperationException()` 填满。
- **S1948 序列化字段**：纯 DTO / POJO / REST 响应对象不实现 `Serializable`（走 Jackson JSON）；异常类不可序列化字段标 `transient` + 注释；禁止在 DTO 字段上标 `transient`（Jackson 会静默丢字段）。
- **S1192 重复字面量**：同一字符串重复 3 次及以上必须提取常量。
- **S3776 认知复杂度 ≤ 15**；**S115 常量全大写**；工作流用户可控输入一律经 `env` 传递（S7630/S7631）。

详细决策树、示例与已知问题修复记录见 [CodeQualitySpec.md](./Docs/DevSpec/CodeQualitySpec.md)。

### 可复用技能（Agent Skills）

SonarCloud 质量扫描循环技能位于 `.agents/skills/sonarcloud-quality-scan/SKILL.md`（Agent Skills 开放标准目录，opencode / Claude Code / Gemini CLI / OpenHands 等可直接发现）：采集 issue → 按类建 GitHub 追踪 → 逐条确认（✅/❌/💡）→ 修复/打回 → 发版复测。配套生成脚本 `scripts/sonarcloud_track.py`。

### 模块命名约定

- 领域模型模块：`model-*`，groupId `com.acanx.meta.model`（如 `model-quote`、`model-deepseek`）
- 组件/SDK 模块：`sdk-*` / `api-*`，groupId `com.acanx.meta.component`
- 基础能力模块：`base-*`，groupId `com.acanx.meta.base`
- 新模块建议用 `tool-archetype` 生成（见 `Docs/Dev/Introduction/NewArtifactGuide.md`），并加入对应父 POM 的 `<modules>`

### 文档文件命名规范

- **Markdown 文档文件名使用大驼峰（PascalCase）命名**，不使用连字符（`-`）、下划线（`_`）或空格。
- 正确示例：`PreReleaseChecklist.md`、`PostReleaseChecklist.md`、`WorkflowTriggerAnalysis.md`、`BomDependencyAnalysis.md`。
- 错误示例：`PreRelease-Checklist.md`、`workflow_trigger.md`。
- 创建新文档时遵循此规范；重命名文件时需同步更新所有引用该文件的链接（含其他 Markdown 文档中的相对链接）。

## 测试指南

测试框架为 JUnit Jupiter 6，测试运行器为 Maven Surefire。测试代码放在对应模块的 `src/test/java` 下；测试资源和 `meta/app.yaml` 或 `meta/app.yml` 放在 `src/test/resources` 下。测试类命名遵循现有 `*Test` 模式，例如 `ArtifactServiceTest` 或 `AppTest`。大范围修改前运行 `mvn test`；局部模块修改可运行 `mvn -pl <module> -am test`。

## 提交与 Pull Request 规范

近期提交历史采用 Conventional Commit 风格，尤其是 `chore(deps): ...`。提交信息建议使用简短前缀，例如 `feat:`、`fix:`、`docs:`、`test:` 或 `chore(deps):`。依赖升级提交应明确写出构件名和版本变化。Pull Request 应说明变更内容、列出受影响模块、关联相关 issue，并提供测试依据，例如 `mvn test` 或目标模块测试命令输出。只有文档或工作流界面变更需要截图。

### Git 工作流红线（必须遵守）

- **禁止**直接推送代码到 `dev`、`main` 等受保护分支
- **必须**通过 PR 流程：特性分支（`feat/`、`fix/`、`docs/`、`refactor/`）→ 推送个人 fork → 向上游发起 PR → 审核合并
- PR 合并前检查目标 PR 状态（`gh pr view <num> --json state,mergedAt`），**已合并的 PR 不得追加代码**，需新建分支和 PR
- 推送后确认 PR 状态与 CI 结果
- 例外：仅当仓库所有者（ACANX）明确授权"直接推送"时方可跳过 PR 流程
- 违反后果：首次严厉批评并检讨，再次终身禁用 Git 操作权限

## 版本管理与发布约定

- **版本单一事实源**：根 `pom.xml` 的 `<revision>` 属性，子模块通过 `${revision}` 继承
- **发版改号**：走 `UpdateProjectVersion` workflow（手动触发），它会同步更新 `revision`、`meta-open.version`、`meta.version`、`bom-graalvm.version`（= `25.<revision>`）
- **不要手动改版本号**：`bom-graalvm.version` 由发版流程程序化派生替换，手动修改会导致版本断层
- **Tag 命名**：`V<version>`（如 `V0.8.9`），由 `ReleaseWorkflow` 在 dev→main 合并后自动创建
- 发布前/后检查请参照 `Docs/Release/PreRelease-Checklist.md` 与 `PostRelease-Checklist.md`

## GitHub Actions 工作流约定

- `UpdateBOMAIODeps`：从 `bom-aio-origin` 的 effective-pom 同步 `bom-aio/pom.xml`；无变更时不创建 PR（属正常行为）
- `UpdateBomGraalvmVersion`：bom-graalvm 版本检查；**派生模式下仅对比提示，不自动覆盖**（避免破坏版本对齐）
- `ReleaseWorkflow`：dev→main 合并后自动打 tag + 创建 GitHub Release；支持 `dry_run=true` 预验证
- 修改 workflow 前先阅读现有文件与 `Docs/Release/README.md`，保持命名与语义一致

## 安全与配置提示

不要提交密钥、签名文件、Maven Central 凭据或本地 IDE 配置。发布配置涉及 GPG、source、Javadoc、flatten 和 Central publishing 插件；修改发布相关配置时应谨慎，并同步检查相关 GitHub Actions 工作流。

## 文档维护

- 新功能/新流程落地时，同步更新 `Docs/` 下对应文档
- 开发相关内容放 `Docs/Dev/Introduction/`，GitHub Action 专题放 `Docs/Dev/GitHubAction/`，发布相关内容放 `Docs/Release/`
- 根 `README.md` 保持项目总览定位（结构、特性、编译、文档入口），不展开细节

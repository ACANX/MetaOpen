# BOM 与依赖声明规范（BomDependencySpec）

> 适用范围：MetaOpen 仓库内所有 `dependencyManagement` / `dependencies` 声明，含 BOM 模块（`os-dependencies`、`meta-bom/bom-*`）、业务模块（`base` / `meta-model` / `meta-component` / `meta-sdk`）的依赖声明与版本引用，以及由工作流生成的 BOM 派生文件（`meta-bom/bom-aio/pom.xml`）。
> 关联文档：[VersionReleaseSpec.md](./VersionReleaseSpec.md)（版本单一事实源、发版）、[GitHubActionWorkflowSpec.md](./GitHubActionWorkflowSpec.md)（§9 可靠性规范）、[ModuleNamingSpec.md](./ModuleNamingSpec.md)、[BomDependencyAnalysis.md](../Dev/Introduction/BomDependencyAnalysis.md)、[PreReleaseChecklist.md](../Release/PreReleaseChecklist.md)
> 最后更新：2026-09-30

---

## 1. 为什么需要本规范

本仓库的依赖管理有两条特殊性质，使「漏写一处 scope」这类小疏忽会直接污染下游**生产部署产物**：

1. **BOM 层层 import**：`bom-aio-origin` 依次 import `os-dependencies` → `bom-deamon` → `bom-mod` → `bom-sdk` → `bom-cf` → `bom-graalvm`，同名 GA **先导入者生效**——上游 BOM 的一个 scope 写法会覆盖下游 BOM 的正确写法；
2. **`dependencyManagement` 的 scope 覆盖语义**：命中传递依赖时**同时覆盖其版本与 scope**（见 §2.2），因此依赖 BOM 的下游无法凭自己的 `test` 声明挽回。

真实事故（详见 §4 与 issue #2806 / #2808 / #2812）：`opentest4j`、`apiguardian-api`、`junit-jupiter-params`、`junit-platform-engine` 四个**纯测试期**库因 scope 误管理为 `compile`，被下游按 `includeScope=runtime` 打包进部署目录 `target/lib`，形成「测试库本体不在、配套库在」的孤儿残留，排查时极易误判依赖树有 bug。

## 2. scope 语义速查（必读）

### 2.1 各 scope 的可见性与传递性

| scope | compile classpath | test classpath | runtime classpath | 对被依赖方传递 |
| --- | --- | --- | --- | --- |
| `compile`（**未声明时的默认值**） | ✅ | ✅ | ✅ | ✅ |
| `provided` | ✅ | ✅ | ❌ | ❌ |
| `runtime` | ❌ | ✅ | ✅ | ✅ |
| `test` | ❌ | ✅ | ❌ | ❌ |

### 2.2 `dependencyManagement` 会覆盖传递依赖

- **未声明 `<scope>` ≡ `compile`**——这是全仓库最容易漏、后果最重的一个缺省值；
- 当 BOM 的 `dependencyManagement` 命中某个**传递**依赖时，Maven **同时用 BOM 中的版本与 scope 覆盖**该传递依赖的原有 scope。因此在 BOM 中把测试库写成 `compile`（或不写），会让下游「本该是 test 的传递依赖」被提升为 compile（#2806 的完整根因链）；
- 反之，`provided` / `test` 在运行期与传递性上都被排除，**不会**随下游打包进入部署产物。

### 2.3 打包过滤语义（下游常见配置）

`maven-dependency-plugin:copy-dependencies` 配 `includeScope=runtime` 时，实际收录 **compile + runtime**，**不含** `provided` / `test`。这就是「BOM 里 scope 写错 → 部署产物多出两个无用 jar」的直接通道。

## 3. 强制条款

### 3.1 测试期构件必须显式声明 `test`

纯测试期构件**必须显式**写 `<scope>test</scope>`，**禁止**依赖缺省值（= `compile`）。建议清单（非穷举，按语义判断）：

```
org.junit.* / org.junit.jupiter:* / org.junit.platform:*     JUnit 系（含 -params、-engine、-commons、-launcher）
org.opentest4j:* / org.apiguardian:*                          junit-jupiter-api 的测试期配套库
org.mockito:* / org.assertj:* / org.hamcrest:*               断言与打桩
org.xmlunit:* / org.hamcrest:*                               XML 比较
```

判断口径：**运行期零用途**（仅在测试编译 / 测试运行期需要）即计入，不限于上表。

### 3.2 可选 / 宿主提供构件必须显式声明 `provided`（并配 `optional`）

由运行容器、宿主框架或部署环境提供的构件（如 `lombok`、Servlet / 容器实现类、Annotaion Processor）：

- 必须显式 `<scope>provided</scope>`；
- 建议同时 `<optional>true</optional>`（与仓库既有约定一致）；
- 同一构件在**所有** BOM 中的 scope 应一致——目前 `lombok` 已统一为 `provided`（见附录 A 说明）。

### 3.3 本仓库构件引用一律写 `${revision}`

BOM 中引用**本仓库自身**的构件时，版本一律写 `${revision}`（版本单一事实源，见 [VersionReleaseSpec.md](./VersionReleaseSpec.md) §1），**禁止写死字面版本号**。

- ✅ 正确（`meta-bom/bom-aio-origin/pom.xml` 的既有写法）：
  ```xml
  <dependency>
      <groupId>com.acanx.meta</groupId>
      <artifactId>os-dependencies</artifactId>
      <version>${revision}</version>
      <type>pom</type>
      <scope>import</scope>
  </dependency>
  ```
- ❌ 错误：`<version>0.9.1</version>` / `<version>0.9.1-SNAPSHOT</version>`（字面值会随版本变更失配，是发布事故的根源，见 §3.4）

### 3.4 生成物规则（`meta-bom/bom-aio/pom.xml`）

`meta-bom/bom-aio/pom.xml` 是 `UpdateBOMAIODeps` 工作流由 `bom-aio-origin` 的 **effective-pom** 生成的派生文件：

1. **禁止手工修改**（含"临时改两行让它过"）；任何需要的修正都要回到源头（`bom-aio-origin` 或其 import 的 BOM）或修正生成逻辑；
2. **生成物中本仓库构件的版本必须与根 `pom.xml` 的 `<revision>` 一致**；
3. **发布态下不得出现 `-SNAPSHOT`**——Maven Central 明确拒绝「release 部署的 `dependencyManagement` 引用 SNAPSHOT 版本」，且该拒绝是**整批 deployment 全有或全无**（真实事故：`bom-aio@0.9.1` 因 23 条 `0.9.1-SNAPSHOT` 被拒，见 §4.2）。

> ⚠️ 由于 effective-pom 会把 `${revision}` **固化**为当时的版本字面值，**版本号变更（`UpdateProjectVersion`）之后必须重新执行 `UpdateBOMAIODeps`**，否则生成物会停留在旧版本上。发版前检查见 [PreReleaseChecklist.md](../Release/PreReleaseChecklist.md) 的「依赖与生成物预检」节。

### 3.5 差异必须登记（台账制），而非强制形式一致

跨 BOM 同一坐标的 `scope` / `optional` **允许**因使用场景不同而不同——**当前的不一致不一定是缺陷**，需要通盘考虑与按场景差异化配置。但：

- **任何跨 BOM scope/optional 差异都必须登记在附录 A 的差异台账中**，登记时写明现状与依据（关联 issue）；
- **未登记的新差异视为违规**——由 `RepoConsistencyAudit` 工作流在 PR 阶段拦下（见 [GitHubActionWorkflowSpec.md](./GitHubActionWorkflowSpec.md) §9.8）；
- 结论无论是「改为一致」还是「保留差异」，都要在台账与关联 issue 内留档，避免同一坐标被反复提出。

## 4. 正确 / 错误示例（均来自真实事故）

### 4.1 测试库漏声明 scope（#2808）

```diff
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-params</artifactId>
      <version>${junit-jupiter.version}</version>
+     <scope>test</scope>   <!-- 漏写时按 Maven 默认即 compile -->
  </dependency>
```

同分组内其余 6 条（`junit-jupiter-api` / `junit-jupiter` / `junit-jupiter-engine` / `junit-platform-commons` / `junit-platform-launcher` / `junit-vintage-engine`）均已正确声明 `test`——**同分组内不一致本身就是高危信号**，review 时应重点看分组注释附近的条目。

### 4.2 生成物固化了 SNAPSHOT 版本（0.9.1 发布事故）

```diff
  <!-- meta-bom/bom-aio/pom.xml（生成物） -->
  <dependency>
      <groupId>com.acanx.meta.base</groupId>
      <artifactId>base-error</artifactId>
-     <version>0.9.1-SNAPSHOT</version>   <!-- ❌ 生成时 revision 还是 SNAPSHOT，被固化 -->
+     <version>0.9.1</version>            <!-- ✅ 在 revision=0.9.1 下重新生成后的结果 -->
  </dependency>
```

事件链：`bom-aio` 于 2026-09-19 在 `revision=0.9.1-SNAPSHOT` 时同步（固化 23 条 SNAPSHOT 字面值）→ 09-30 版本改为 `0.9.1` 但**未重新同步** → 发版时被 Central 以 "Dependency management dependencies to SNAPSHOT versions not allowed" 拒绝（23 条全中）。

### 4.3 上游 BOM 覆盖下游的正确声明（#2806）

`junit-jupiter-api:test` 的两个传递依赖 `opentest4j` / `apiguardian-api`，在下游本应是 `test`（传递依赖 scope 收敛），但 `os-dependencies` 把它们显式管理为 `compile` → 下游 `dependency:tree` 显示：

```
\- org.junit.jupiter:junit-jupiter-api:jar:6.1.3:test
   +- org.opentest4j:opentest4j:jar:1.3.0:compile        ← 应为 test
   \- org.apiguardian:apiguardian-api:jar:1.1.2:compile  ← 应为 test
```

**结论：BOM 是「全局默认值」，写错一处等于给所有下游改了默认值。**

## 5. 附录 A：跨 BOM scope 差异台账

> **用途**：登记跨 BOM 同一坐标的 scope/optional 差异，供 `RepoConsistencyAudit` 工作流比对（见 §3.5）。
> **维护规则**：有增删改必须在本表内同步更新，并在「依据」列写明关联 issue；未登记的差异会让审计失败。
> **机器可读区间**：下方 `BOM_SCOPE_BASELINE` 注释对之间的表格由脚本解析，**请勿改动注释行与列顺序**。
> 数据来源：`origin/dev`；`—` = 该文件未管理此坐标；`compile(未声明)` 归一见 §2.2。

<!-- BOM_SCOPE_BASELINE:BEGIN -->
| # | 坐标 | `os-dependencies` | `bom-deamon` | `bom-mod` |
| --- | --- | --- | --- | --- |
| 1 | `com.alibaba.nacos:nacos-client` | `provided` / opt=true | `compile` | — |
| 2 | `commons-codec:commons-codec` | `provided` / opt=true | `compile` | `compile` |
| 3 | `io.netty:netty-all` | `provided` / opt=true | `compile` | — |
| 4 | `jakarta.validation:jakarta.validation-api` | `provided` / opt=true | `compile` | — |
| 5 | `org.apache.dubbo:dubbo` | `provided` / opt=true | `compile` | — |
| 6 | `org.hibernate.validator:hibernate-validator` | `provided` / opt=true | `compile` | — |
| 7 | `org.mybatis:mybatis` | `provided` / opt=true | `compile` | — |
| 8 | `org.springframework:spring-core` | `provided` / opt=true | `compile` | — |
| 9 | `org.springframework.boot:spring-boot` | `provided` / opt=true | `compile` | — |
| 10 | `org.springframework.security:spring-security-core` | `provided` / opt=true | `compile` | — |
| 11 | `org.xerial:sqlite-jdbc` | `provided` / opt=true | `compile` | `compile` |
| 12 | `org.jspecify:jspecify` | `provided` | `compile` | `compile` |
| 13 | `com.google.protobuf:protobuf-java` | `provided` / opt=true | — | `compile` |
| 14 | `org.jsoup:jsoup` | `provided` | — | `compile` |
| 15 | `org.slf4j:slf4j-api` | `provided` / opt=true | — | `compile` |
| 16 | `net.bytebuddy:byte-buddy` | `provided` | — | `compile` |
<!-- BOM_SCOPE_BASELINE:END -->

| 类别 | 坐标 | 结论与依据 |
| --- | --- | --- |
| **已统一**（本台账不含） | `org.projectlombok:lombok` | 已统一为 `provided`（四个 BOM 及 `bom-aio` 生成物实测一致），随 `#2960` 落地、`0.9.1` 发布；登记见 issue #2812 |
| **待逐条场景讨论** | 上表第 1~16 条 | 按「不强制形式一致、需按场景差异化、待逐条分析达成一致后再调整」的判定**保持现状**；讨论口径见 issue #2812 的评论：①该坐标是编译期 API / 运行期必需 / 可选或宿主提供？②通过 `bom-aio`（`provided` 生效）与直接 import `bom-deamon`/`bom-mod`（`compile`）两条路径的实际差异？③若需一致，以哪一侧为准、对成品 BOM 与下游的影响？ |

## 6. 检查清单（PR 提交前自查）

- [ ] 新增 / 修改的依赖声明**显式**写了 `<scope>`（未依赖缺省值）
- [ ] 测试期构件（§3.1 清单及同类）为 `test`
- [ ] 可选 / 宿主提供构件为 `provided`（建议叠加 `optional=true`）
- [ ] 引用本仓库构件一律 `${revision}`，无字面版本号
- [ ] 未手工修改 `meta-bom/bom-aio/pom.xml`（生成物）
- [ ] 若改动波及跨 BOM scope/optional 差异，已同步更新附录 A 台账并在「依据」写明关联 issue
- [ ] 版本号变更（`UpdateProjectVersion`）之后，已重新执行 `UpdateBOMAIODeps` 并确认生成物版本与新 `<revision>` 一致

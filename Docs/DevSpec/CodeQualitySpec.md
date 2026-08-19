# Java 代码质量红线规范（CodeQualitySpec）

> 适用范围：MetaOpen 仓库内所有 Java 源代码（`src/main/java`、`src/test/java`）及 GitHub Actions 工作流（`.github/workflows/`）。
> 状态：**强制要求**。本规范为 SonarCloud 静态扫描（SonarCloudCodeAnalysis 工作流，push / PR 自动分析）的配套红线，后续开发必须遵守，避免再次出现已修复的已知问题。
> 最后更新：2026-08-19

---

## 1. 总则

- SonarCloud 是项目静态扫描的**强制环节**：push 与 PR 均由 `SonarCloudCodeAnalysis` 工作流自动分析，结果通过 SonarCloud 门禁（Quality Gate）与 `SonarCloud Code Analysis` PR 检查反馈。
- **红线一：新代码 / 修改代码不得新增任何 SonarCloud issue**；若因业务必要无法避免，必须在 PR 描述中说明原因并给出豁免方式（注释 / `// NOSONAR` / `@SuppressWarnings`）。
- **红线二：HIGH 影响（CRITICAL 及以上）的问题必须在本 PR 内解决**，不得遗留到后续 PR。
- **红线三：本规范第 7 节列出的已知问题已全部修复，任何改动不得使其回退复现。**

## 2. S1186 空方法 / 空构造器

> 规则：空方法或空构造器必须说明存在原因，否则视为待办遗留或错误设计。

### 2.1 决策树

| 场景 | 处理方式 |
|------|----------|
| 类**没有其他构造器**，且无框架需要 | 直接删除显式空构造器（Java 编译器会自动生成等价默认构造器，字节码层面无差异） |
| 类**有其他带参构造器** + 框架需要无参构造器 | **保留** + 方法体内嵌套注释（见 2.2） |
| 类有其他带参构造器 + 框架不需要 | 删除；反序列化需求改用 `@JsonCreator` 等替代方案 |
| **不确定**（POJO / DTO / 模型类） | 保守处理：保留 + 嵌套注释（防将来新增带参构造器时踩坑） |

### 2.2 必须保留时的写法（官方推荐：嵌套注释）

```java
public EmailMessage() {
    // Lombok @Data 不生成构造器，Jackson 反序列化需要默认无参构造器，不可删除
}
```

- ✅ 方法体**内部**加注释说明为什么为空（SonarCloud 官方提示 "Add a nested comment explaining why this method is empty"）。
- ✅ 备选豁免：`@SuppressWarnings("java:S1186")`（方法级）或行尾 `// NOSONAR`。
- ❌ **禁止**用 `throw new UnsupportedOperationException()` "填满"空构造器——框架实例化时直接抛异常，比空构造器更严重。

### 2.3 需要无参构造器的常见框架（判断依据）

Jackson / Gson 反序列化、JPA / Hibernate 实体、MyBatis resultType 映射、JavaBeans 规范（`java.beans.Introspector`）、Spring CGLIB 代理（`@Configuration` / AOP）、反射实例化（`newInstance()`）。

### 2.4 正确 / 错误示例

```java
// ✅ 正确：框架需要，嵌套注释说明
public MVSVMetadata() {
    // Jackson 等框架反序列化需要默认无参构造器，不可删除
}

// ✅ 正确：无其他构造器且无需保留，直接删除（编译器自动生成等价构造器）

// ❌ 错误：空构造器无注释，SonarCloud 报 S1186
public MVSVMetadata() {
}
```

## 3. S1948 序列化字段

> 规则：实现 `Serializable` 的类中，非 static 字段必须是可序列化类型或 `transient`。

### 3.1 总原则

- **纯 DTO / POJO / REST 响应对象一律不实现 `Serializable`**。HTTP 场景序列化走 Jackson JSON，不需要 `implements Serializable`；实现反而引入 S1948 风险面。
- 泛型字段（如 `private T data;`）是 S1948 高发点：泛型 `T` 无 `extends Serializable` 约束，SonarCloud 无法确认可序列化。

### 3.2 各类对象的处理方式

| 对象类型 | 处理方式 |
|----------|----------|
| REST 响应 / 请求 DTO、POJO、模型类 | **移除 `implements Serializable`**（连同 `serialVersionUID`、`import java.io.Serializable`），并在类注释说明走 JSON 序列化 |
| 异常类（继承 `RuntimeException` / `Throwable`，本身即 Serializable） | 不可序列化字段标 `transient` + 注释说明（见 3.3） |
| 必须实现 Serializable 的业务类 | 泛型字段加 `T extends Serializable` 约束；或字段标 `transient`（仅在确认不参与该对象序列化场景时） |

### 3.3 异常类字段处理（transient 的正确使用场景）

```java
/**
 * 国际化参数（高度通用，用于消息模板替换）
 *
 * <p>transient：仅在本地用于消息模板替换，跨进程序列化（RMI/分布式）时无需携带，
 * 避免 java:S1948 告警（Object[] 元素可能不可序列化）。</p>
 */
private final transient Object[] args;
```

### 3.4 ⚠️ transient 陷阱（重点）

- `transient` 只适用于**不参与 Jackson JSON 序列化**的对象（如异常类——异常不直接作为 JSON 响应体）。
- **禁止**在 DTO / REST 响应对象的字段上标 `transient`：Jackson 默认跳过 transient 字段，会导致该字段**从 API 响应中消失**（静默丢数据，比告警更严重）。
- 泛型约束 `T extends Serializable` 会**破坏 API 兼容性**（调用方传非 Serializable 类型直接编译失败），不推荐。

### 3.5 正确 / 错误示例

```java
// ✅ 正确：REST 响应对象不实现 Serializable
public class RestResult<T> {  // 走 Jackson JSON 序列化
    private T data;
}

// ✅ 正确：异常类不可序列化字段标 transient + 注释
private final transient Map<String, Object> details;

// ❌ 错误：DTO 实现 Serializable 且泛型字段无约束 → S1948
public class RestResult<T> implements Serializable {
    private T data;
}

// ❌ 错误：DTO 字段标 transient → Jackson 序列化时字段静默丢失
public class RestResult<T> {
    private transient T data;  // data 从 JSON 响应中消失！
}
```

## 4. S1192 重复字符串字面量

> 规则：同一字符串字面量在代码中重复 3 次及以上，必须提取为常量。

```java
// ✅ 正确：提取常量
public static final String CHARSET_UTF8_SUFFIX = "; charset=UTF-8";
public static final String MEDIA_TYPE_APPLICATION_JSON_UTF8 = MEDIA_TYPE_APPLICATION_JSON + CHARSET_UTF8_SUFFIX;

// ❌ 错误：字面量重复 3 次 → S1192
public static final String MEDIA_TYPE_APPLICATION_JSON_UTF8 = MEDIA_TYPE_APPLICATION_JSON + "; charset=UTF-8";
```

## 5. 其他 HIGH 规则速查

| 规则 | 含义 | 处理要求 |
|------|------|----------|
| S115 | 常量命名不符合 `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | 常量一律全大写 + 下划线 |
| S3776 | 方法认知复杂度超过阈值（默认 15） | 拆分子方法，控制分支 / 嵌套层级 |
| S7630 | GitHub Actions 脚本注入（`${{ }}` 内插用户可控输入） | 用户可控输入一律经 `env` 变量传递，禁止在 `run` 块直接内插 |
| S7631 | GitHub Actions 执行不受信任代码（pull_request_target 场景） | 仅限可信协作者触发；checkout fork 代码须配合 review 与最小权限 token |

## 6. 检查清单（PR 提交前自查）

- [ ] SonarCloud Code Analysis 检查通过，无新增 issue
- [ ] 空方法 / 空构造器已按 §2 处理（删除或嵌套注释），无裸空构造器
- [ ] DTO / POJO / REST 响应对象未实现 `Serializable`；异常类 transient 字段已加注释
- [ ] 重复 3 次及以上的字符串字面量已提取常量
- [ ] 方法认知复杂度 ≤ 15；常量命名全大写 + 下划线
- [ ] GitHub Actions 工作流无 `${{ }}` 直接内插用户可控输入

## 7. 已知问题修复记录（2026-08-19）

以下 SonarCloud HIGH 影响问题已按本规范修复（随 PR 合并至 `dev`），**不得回退**：

| Issue Key | 规则 | 位置 | 修复方案 |
|-----------|------|------|----------|
| AZ6MITdqyM7RUo22KD2C | S1186 | `base/base-file/.../MVSVMetadata.java:65` | 保留 + 嵌套注释 |
| AZ6MITjwyM7RUo22KD2x | S1186 | `meta-model/model-mail/.../EmailMessage.java:31` | 保留 + 嵌套注释 |
| AZ6MITc5yM7RUo22KD18 | S1186 | `base/base-rest/.../domain/Void.java:11` | 保留 + 嵌套注释 |
| AZ6MITdAyM7RUo22KD19 | S1948 | `base/base-rest/.../RestResult.java:44` | 移除 `implements Serializable` |
| AZ6MITcgyM7RUo22KD11 | S1948 | `base/base-exception/.../BaseException.java:27` | `args` 标 transient + 注释 |
| AZ6MITcwyM7RUo22KD17 | S1948 | `base/base-exception/.../BusinessException.java:30` | `details` 标 transient + 注释 |
| AZ6MITcOyM7RUo22KD1x | S1192 | `base/base-http/.../BaseHttpConst.java:55` | 提取 `CHARSET_UTF8_SUFFIX` 常量 |

> 关联规范：编码风格见 [CodeStyleSpec.md](./CodeStyleSpec.md)、测试规范见 [TestSpec.md](./TestSpec.md)、PR 流程见 [GitCommitPRSpec.md](./GitCommitPRSpec.md)。

# Java 编码风格与命名约定（CodeStyleSpec）

> 适用范围：MetaOpen 仓库内所有 Java 源代码（`src/main/java`、`src/test/java`）。
> 最后更新：2026-08-19

---

## 1. 编码基础

- 统一使用 **UTF-8** 编码，源码文件带 UTF-8 头（如 `-Dfile.encoding=UTF-8`），确保跨平台构建一致。
- 使用父 POM 中定义的 **Java 21** 配置，保持全仓库 JDK 版本统一。
- 保持现有 Java 风格，不引入额外的代码风格插件强制约束（当前以人工审查 + 约定为准）。

## 2. 缩进与排版

- **4 空格缩进**，不使用 Tab。
- 行宽建议不超过 120 字符，超长表达式换行并对齐。

## 3. 命名约定

| 元素 | 风格 | 示例 |
|------|------|------|
| 包名 | 全小写，位于 `com.acanx.meta` 下 | `com.acanx.meta.model.quote` |
| 类 / 接口 | PascalCase（大驼峰） | `ArtifactService`、`QuoteModel` |
| 方法 | camelCase（小驼峰），动词开头 | `getQuote()`、`buildArtifact()` |
| 字段 / 局部变量 | camelCase | `quoteId`、`maxRetry` |
| 常量 | 全大写 + 下划线 | `MAX_RETRY_COUNT`、`DEFAULT_TIMEOUT` |
| 枚举值 | 全大写 + 下划线 | `OrderStatus.PENDING` |

- 正确示例：
  ```java
  public class QuoteService {
      private static final int MAX_RETRY_COUNT = 3;

      public QuoteModel getQuote(String symbol) {
          // ...
      }
  }
  ```
- 错误示例：
  - `class quote_service` ❌（类名应用 PascalCase）
  - `int MaxRetry` ❌（字段应用 camelCase）
  - `final int max_retry_count = 3` ❌（常量应全大写）

## 4. 代码组织

- **优先使用模块内的小型模型类和工具类**，避免过早抽象。
- 只有在**复用价值明确**时才将类抽象到共享层（如 `base-*` 模块）。
- 保持类职责单一，一个文件一个公开类（内部类除外）。

## 5. 注释与文档

- Javadoc 注释优先使用**简体中文**（与仓库交流语言一致）。
- 公开 API（类、方法）建议补充 Javadoc，说明用途、参数与返回值。
- 不写与代码无关的注释；修改代码时同步更新相关注释。

## 6. 检查清单

- [ ] 文件 UTF-8 编码、Java 21 语法
- [ ] 4 空格缩进，无 Tab
- [ ] 包名 `com.acanx.meta.*` 全小写
- [ ] 类 PascalCase、方法/字段 camelCase、常量全大写
- [ ] 注释为简体中文，公开 API 有 Javadoc

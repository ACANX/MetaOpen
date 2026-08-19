# 参考标准：上游已创建的追踪 issue 样例

> 本节收录 ACANX/MetaOpen 已创建的真实追踪 issue 完整 body，作为生成格式的**参考标准**。
> 后续任何项目生成追踪 issue 时，结构、措辞、格式必须与此保持一致，不得自由发挥。

## 一、已建 issue 清单（MetaOpen，2026-08-19）

| issue | 标题 | 类别前缀 | 覆盖规则 | 数量 |
|-------|------|---------|---------|------|
| #2535 | [SonarCloud][工作流] 脚本注入与敏感信息加固（S7630/S7636/S8541/S8544） | `SC` | S7630×4、S7636×8、S8541/S8544×2 | 14 |
| #2536 | [SonarCloud][工作流] 第三方 Action 固定完整 SHA（S7637） | `SCF` | S7637×8 | 8 |
| #2537 | [SonarCloud][Java] 清理注释掉的代码（S125） | `SJ1` | S125×44 | 44 |
| #2538 | [SonarCloud][Java] MVSVSerializer 重构专项（S3776/S3457） | `SJ2` | S3776×2、S3457×14 | 16 |
| #2539 | [SonarCloud][Java] 工具类/常量类添加私有构造器（S1118） | `SJ3` | S1118×12 | 12 |
| #2540 | [SonarCloud][Java] 日志与废弃代码规范（S106/S6355/S1123/S1133） | `SJ4` | S106×8、S6355/S1123/S1133×15 | 23 |
| #2541 | [SonarCloud][Java] 命名/import/逻辑杂项（S115/S1128/S1066/S2209/S1172/S2440/S112/S5961） | `SJ5` | S115×4、S1128×3、S1066×2、S2209×2、S1172、S2440、S112、S5961 | 15 |

合计覆盖 132 个问题（139 总 − 7 个已随 PR #2523 修复的 S1186/S1948/S1192）。

## 二、body 结构模板（必须严格遵循）

```markdown
## 背景

来源：SonarCloud [<PROJECT>](<sonarcloud issues URL>?issueStatuses=OPEN%2CCONFIRMED&id=<PROJECT>)（OPEN/CONFIRMED 共 <N> 个）。本 issue 覆盖 **<M> 个**问题。

## 交互方式（重要）

请逐条回复你的决策，格式（直接在评论区回复，或编辑本 body 的决策清单）：

```
- <PREFIX>-1 ✅ 按建议修
- <PREFIX>-2 ❌ 不修，原因：保留作为参考
- <PREFIX>-3 💡 改方案：<你的想法>
```

我收到反馈后：✅ → 安排修复 PR；❌ → 在 SonarCloud 标记 Won't Fix/False Positive；💡 → 按新方案调整后再确认。

## 决策清单（待你确认，勾选 = 按建议修）

- [ ] `<PREFIX>-1` `<ruleKey>` <SEVERITY> · `<path>:<line>` — <一句话问题说明>
- [ ] `<PREFIX>-2` `<ruleKey>` <SEVERITY> · `<path>:<line>` — <一句话问题说明>
...（每条一行，按行号顺序）

## 类别说明

- <规则> ×<数量>：<修复说明 / 已修复待验证标注 / 建议分 PR 策略>

---

## 逐条详情

### <PREFIX>-1 `<ruleKey>` <SEVERITY> · <完整路径>:<行号>
- **问题**：<问题说明>
- **官方提示**：<SonarCloud 官方提示全文>
- **建议方案**：<可落地的修复方案>
- **代码摘录**（行 <N> 附近）：
```
   <N-2>| <上下文代码>
   <N-1>| <上下文代码>
>> <N>|   <目标行代码>
   <N+1>| <上下文代码>
```

### <PREFIX>-2 ...
（每条一个二级标题，字段顺序固定：问题 → 官方提示 → 建议方案 → 代码摘录）
```

## 三、格式硬性要求

1. **标题**：`[SonarCloud][工作流|Java] <类别主题>（<规则列表>）`；每类一个 issue
2. **编号**：类别前缀 + 序号（`SC-1`、`SCF-2`、`SJ1-3`…），全局唯一、连续
3. **决策清单**：每行 checkbox，行内必须含 `编号 · 规则 · 严重度 · 路径:行号 — 简述`，勾选 = 按建议修；S125 类在简述括号内附被注释的实际内容（如 `（<!-- <dependency>）`）
4. **交互模板**：每个 body 顶部必须放 ✅/❌/💡 三行示例，说明决策后动作
5. **逐条详情**：`### 编号 规则 严重度 · 完整路径:行号` 二级标题；字段顺序固定：问题 → 官方提示 → 建议方案 → 代码摘录；代码摘录含行号标注，目标行以 `>>` 前缀突出
6. **类别说明段**：放决策清单与逐条详情之间，说明各类规则数量、是否已修复待验证、拆分 PR 建议
7. **已修复待验证**：不建追踪 issue，在汇报中说明（如 S7630 已随 #2521 修复）

## 四、真实完整样例

- #2535（工作流安全，SC）：`gh issue view 2535 --repo ACANX/MetaOpen --json body -q .body`
- #2537（S125 注释代码，SJ1）：`gh issue view 2537 --repo ACANX/MetaOpen --json body -q .body`
- #2536（Action 固定 SHA，SCF）：`gh issue view 2536 --repo ACANX/MetaOpen --json body -q .body`
- #2538（复杂度/格式专项，SJ2）：`gh issue view 2538 --repo ACANX/MetaOpen --json body -q .body`

> 生成时优先复用 `scripts/sonarcloud_track.py`（输出即此格式）；如手写，必须逐条对照本模板与上述真实样例。

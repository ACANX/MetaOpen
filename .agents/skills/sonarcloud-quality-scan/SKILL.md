---
name: sonarcloud-quality-scan
description: SonarCloud 代码质量扫描循环：采集 issue→按类建 GitHub 追踪 issue→逐条确认（改/不改/改方案）→修复/打回→发版复测
license: MIT
compatibility: opencode, claude-code, gemini-cli, openhands
metadata:
  audience: agents-developers
  workflow: quality-scan
---

# SonarCloud 代码质量扫描循环（SonarCloudQualityScan）

> 可复用的质量巡检工作流：**采集 → 分类 → 建追踪 → 确认 → 修复/打回 → 发版复测**。
> 适用于 SonarCloud 项目的周期性质量扫描，供各类 Agent / 开发者直接复用。

---

## 1. 适用场景

- 用户要求"扫描代码质量 / 整理 SonarCloud issue / 处理质量告警 / 复测验证"
- 定期质量巡检（新 issue 出现时）
- 修复合并发版后的复测闭环

## 2. 前置条件

- `gh` CLI 已登录且对目标仓库有写权限（创建 issue / PR）
- 网络可达 `sonarcloud.io`（公开 API 匿名可读，无需 token 即可采集 issue）
- 本地有仓库克隆（用于代码摘录；无克隆时摘录标注"以 SonarCloud 为准"）
- 参数：SonarCloud organization、project key（MetaOpen：`acanx` / `ACANX_MetaOpen`）、目标分支（`dev`）

## 3. 工作流总览

```
① 采集   → SonarCloud API 拉取 OPEN/CONFIRMED issues（全量）
② 分类   → 按"实际问题类别"分组（同类规则/同类处置合并，非纯按规则）
③ 生成   → 每类生成 GitHub issue body（决策清单 + 逐条详情）
④ 建/更新 → gh issue create / gh issue edit
⑤ 确认   → 用户逐条回复 ✅按建议修 / ❌不修 / 💡改方案
⑥ 落实   → ✅fork PR（Closes #issue） / ❌SonarCloud 标 Won't Fix / 💡调整方案
⑦ 复测   → 合并发版后重新扫描，验证 issue FIXED/CLOSED，更新追踪 issue
```

## 4. 步骤详解

### ① 采集

```bash
curl -s "https://sonarcloud.io/api/issues/search?organization=<ORG>&projects=<PROJECT>&issueStatuses=OPEN%2CCONFIRMED&ps=500"
```

保存原始 JSON，统计总数与按规则分组数量。常用附加过滤：`&impactSeverities=HIGH`（只看高影响）、`&rules=java:S125`（单规则）。

### ② 分类

按**实际问题类别**分组（参考脚本内置默认类别）：
- 工作流安全类（S7630 注入 / S7636 secrets / S7637 SHA / S8541 / S8544）
- Java 代码类（S125 注释代码 / S3776+S3457 复杂度格式 / S1118 构造器 / S106+S6355+S1123+S1133 日志废弃 / 杂项）

### ③ 生成追踪 issue body

```bash
python3 scripts/sonarcloud_track.py \
  --org <ORG> --project <PROJECT> \
  --repo-dir <本地仓库路径> --output-dir <输出目录>
```

每条目包含：**编号（类别前缀+序号）· 规则 · 严重度 · 文件:行号 · 问题简述 · 官方提示全文 · 建议方案 · 本地代码摘录**。每类 body 顶部有**决策清单**（checkbox，行内带规则/位置/简述，勾选 = 按建议修）和**交互模板**。自定义类别：`--categories categories.json`。

### ④ 创建 / 更新 GitHub issue

```bash
gh issue create --repo <owner>/<repo> --title "<类别标题>" --body-file <key>.md   # 新建（每类一个）
gh issue edit <num> --repo <owner>/<repo> --body-file <key>.md                    # 更新（决策反馈后）
```

标题统一前缀 `[SonarCloud][工作流|Java] ...`。已修复待验证的问题不建追踪 issue，在汇报中说明。

### ⑤ 交互确认

用户回复格式（或直接编辑 body 勾选 checkbox）：

```
- SC-1 ✅ 按建议修
- SJ1-7 ❌ 不修，原因：保留作为参考
- SJ2-1 💡 改方案：把阈值调到 25
```

### ⑥ 落实

| 用户决策 | 动作 |
|---------|------|
| ✅ 按建议修 | 汇总同类 → fork 特性分支 → 修复 → 推送 fork → PR 到目标分支，描述带 `Closes #<追踪issue>` |
| ❌ 不修 | SonarCloud API 标记：`POST /api/issues/do_transition?issue=<key>&transition=wontfix`（或 `falsepositive`），需项目管理员权限；原因记录进追踪 issue |
| 💡 改方案 | 按用户方案调整 → 回用户确认 → 再落实 |

- **Git 红线**：代码变更一律走 fork → PR（见 `Docs/DevSpec/GitCommitPRSpec.md`），禁止直推受保护分支
- 代码级豁免：行尾 `// NOSONAR[ruleKey]` 或 Java `@SuppressWarnings("java:Sxxxx")`，需走 PR

### ⑦ 发版复测

1. 合并 PR 到目标分支（dev），触发 push 扫描（PR 分析走 pull_request_target）
2. 等待 SonarCloud 重新分析完成
3. 复测查询：`issueStatuses=OPEN,CONFIRMED,FIXED,CLOSED` 或按 issue key 查状态
4. 已修复确认 `FIXED/CLOSED`；仍 OPEN 的检查是否漏改/行号偏移
5. 更新追踪 issue：勾选完成项，关闭已清零的类别 issue，汇报闭环

## 5. 规则知识库速查

| 规则 | 问题 | 建议方案 |
|------|------|---------|
| S7630 | run 块内插用户可控输入 | 经 env 传递 |
| S7636 | run 块展开 secrets | secrets → env → $VAR |
| S7637 | Action 未固定 SHA | 完整 40 位 SHA + Dependabot |
| S8541/S8544 | pip 无二进制限制/未锁版本 | --only-binary :all: + 锁文件 |
| S125 | 注释掉的代码 | 删除（或移文档，不留死代码） |
| S3457 | printf 用 \n | 改 %n |
| S3776 | 认知复杂度 >15 | 拆方法（或调阈值需确认） |
| S1118 | 工具类可被实例化 | 加 private 构造器 |
| S106 | System.out | 换 SLF4J Logger |
| S6355/S1123/S1133 | @Deprecated 标注不全/遗留 | 补 since/forRemoval + @deprecated；评估移除 |
| S115 | 常量命名 | 全大写+下划线 |
| S1128/S1066/S2209/S1172/S2440/S112/S5961 | import/嵌套 if/静态引用/未用参数/实例化/泛型异常/断言多 | 见脚本 RULE_KB 详情 |
| S1186 | 空构造器 | 框架需要→嵌套注释；否则删（决策树见 CodeQualitySpec §2） |
| S1948 | 序列化字段 | DTO 移除 Serializable；异常 transient（CodeQualitySpec §3） |
| S1192 | 重复字面量 | 提取常量 |

## 6. 注意事项

- **用户决策优先**：SonarCloud 建议 ≠ 必须采纳；用户 ❌/💡 一律尊重并记录
- **行号偏移**：issue 行号基于最近一次分析，本地代码可能已变，代码摘录仅供参考
- **分析挂分支**：pull_request_target 场景若未显式传 `sonar.pullRequest.*`，PR 分析可能落错分支，复测时以目标分支 push 扫描为准
- **权限**：Won't Fix / False Positive 标记需要 SonarCloud 项目管理员权限
- **规范关联**：修复方案与红线以 `Docs/DevSpec/CodeQualitySpec.md` 为准

## 7. 检查清单

- [ ] 采集数据保存（原始 JSON + 统计）
- [ ] 分类覆盖全部 OPEN/CONFIRMED issues（总数对得上）
- [ ] 每类追踪 issue 有决策清单 + 逐条详情（编号连续）
- [ ] 已修复待验证的问题已标注（不重复建追踪）
- [ ] 用户反馈已同步（✅ 建 PR / ❌ 标记 / 💡 调整）
- [ ] PR 描述带 `Closes #<追踪issue>`
- [ ] 合并后复测，确认 FIXED/CLOSED，更新追踪 issue 状态

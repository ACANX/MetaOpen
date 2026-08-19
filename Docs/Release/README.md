# MetaOpen 正式版发布方案与技术实现

> 本文档介绍 MetaOpen 项目正式版（Release）发布的完整技术方案，包括版本管理机制、自动化打 tag/发布工作流，以及截至当前版本（0.8.9）的实现状态。
>
> 最后更新：2026-08-19

---

## 1. 版本演进历史

MetaOpen 的版本号遵循语义化版本（SemVer）风格，格式为 `0.x.y`，通过 Maven 的 CI-Friendly 版本机制（`revision` 属性）统一管理。

| 版本 | Tag | 发布日期 | 说明 |
|------|-----|---------|------|
| V0.6.1 | `V0.6.1` | 2025-12-31 | 早期版本 |
| V0.7.3 | `V0.7.3` | 2026-03-27 | 中期版本 |
| V0.8.2.3 | `V0.8.2.3` | 2026-05-01 | Pre-release |
| V0.8.4 | `V0.8.4` | 2026-05-08 | 正式版 |
| V0.8.5 | `V0.8.5` | 2026-06-03 | 正式版 |
| V0.8.6 | `V0.8.6` | 2026-06-13 | 正式版 |
| V0.8.7 | `V0.8.7` | 2026-06-16 | 正式版 |
| V0.8.8 | `V0.8.8` | 2026-07-04 | 正式版（Latest） |
| 0.8.9 | （待发布） | - | 当前开发版本 |

**Tag 命名规范**：`V + 版本号`（如 `V0.8.8`），历史上由人工创建。

---

## 2. 版本号管理机制

### 2.1 单一事实源

项目版本号的**唯一事实源**是根 `pom.xml` 中的 `<revision>` 属性：

```xml
<!-- 根 pom.xml -->
<properties>
    <revision>0.8.9</revision>
</properties>
```

所有子模块通过 `${revision}` 继承版本，保证全仓库版本一致。

### 2.2 发版改号流程（UpdateProjectVersion）

发布新版本时，通过 `.github/workflows/UpdateProjectVersion.yml` 自动修改版本号：

- **触发方式**：`workflow_dispatch`（手动触发，可输入目标版本号）
- **修改范围**（`DIRS`）：
  - `.`（根 pom.xml）
  - `./os-dependencies`
  - `./meta-bom`
  - `./meta-bom/bom-aio-origin`
- **修改属性**（`PROPERTIES`）：
  - `revision` → 新版本号
  - `meta-open.version` → 新版本号
  - `meta.version` → 新版本号
  - `bom-graalvm.version` → `25.<新版本号>`（派生属性，自动加前缀）
- 同时更新根目录 `version` 文件

### 2.3 bom-graalvm 版本对齐（2026-08-19 修复）

**历史问题**：`bom-aio-origin/pom.xml` 中 `bom-graalvm.version` 为硬编码（如 `25.0.8.8`），与本地 `bom-graalvm` 模块的派生版本（`25.${revision}`）脱节。发版时 `UpdateProjectVersion` 只更新 `revision`，不更新 `bom-graalvm.version`，导致每次发版后 `bom-aio-origin` 引用的 bom-graalvm 落后于本地模块，`UpdateBOMAIODeps` 解析 Maven Central 上的旧版 bom-graalvm 时产生 **graalvm 依赖降级**（1.1.9 → 1.1.3）。

**修复方案（PR #2505）**：
- `.github/Python/UpdatMeavenProperties.py` 新增派生属性映射（`DERIVED_PREFIX`）：
  ```python
  DERIVED_PREFIX = {
      "bom-graalvm.version": "25.",   # 实际值 = 25.<new_version>
  }
  ```
- `UpdateProjectVersion.yml` 的 `PROPERTIES` 列表加入 `bom-graalvm.version`
- 发版时 `bom-graalvm.version` 被程序强制替换为 `25.<新版本号>`，与 bom-graalvm 模块版本自动对齐

**效果**：发版零手动维护，版本号统一由 `UpdateProjectVersion` 管理。

---

## 3. 正式版发布流程（自动化）

### 3.1 总体流程

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. UpdateProjectVersion（发版改号）                              │
│    - revision/meta-open.version/meta.version/bom-graalvm.version │
│    - PR → 合并到 dev                                              │
├─────────────────────────────────────────────────────────────────┤
│ 2. dev 分支验证                                                   │
│    - MultiMavenJDKBranchCI / SonarQube / CodeQL 等 CI            │
│    - UpdateBOMAIODeps（bom-aio 依赖同步校验）                     │
├─────────────────────────────────────────────────────────────────┤
│ 3. dev → main 合并（PR）                                          │
│    - 代码审查 → 合并                                               │
├─────────────────────────────────────────────────────────────────┤
│ 4. ReleaseWorkflow 自动触发（push: branches: [main]）             │
│    - 自动提取版本号 → 打 annotated tag → 创建 GitHub Release      │
│    （本步骤由 PR #2506 新增）                                     │
├─────────────────────────────────────────────────────────────────┤
│ 5. （可选）ReleaseFullArtifactsByBatch                            │
│    - 手动触发，发布 Maven 制品到 Maven Central                    │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 自动打 Tag 工作流（ReleaseWorkflow）

新增的 `.github/workflows/ReleaseWorkflow.yml`（PR #2506）：

```yaml
name: ReleaseWorkflow
on:
  push:
    branches: [main]          # dev → main 合并后自动触发
  workflow_dispatch:          # 手动触发（支持 dry_run 验证）
```

**执行步骤**：

| 步骤 | 功能 |
|------|------|
| `ExtractVersionFromPom` | 从根 `pom.xml` 的 `<revision>` 提取版本号（单一事实源），生成 `V<revision>` tag 名 |
| `CheckIfTagExists` | 幂等保护：tag 已存在则跳过（防止重复触发报错） |
| `DryRunVerify` | `dry_run=true` 时仅打印将要执行的动作，不实际创建 |
| `CreateAndPushTag` | 创建 **annotated tag**（`git tag -a V0.8.9 -m "Release V0.8.9"`）并推送 |
| `CreateGitHubRelease` | 用 `softprops/action-gh-release` 创建 GitHub Release，`generate_release_notes: true` 自动生成 changelog |

**权限要求**：`permissions: contents: write`（用于推送 tag 和创建 Release）。

### 3.3 首次合并触发问题（重要）

**问题**：ReleaseWorkflow 文件首次进入 main 分支（即第一次 dev→main 合并）时，workflow 尚未存在于默认分支，`push` 事件能否触发？

**答案：能触发，无需等待下一个版本。**

**原理**：GitHub Actions 的 `push: branches: [main]` 事件在分支被推送时，读取的是 **push 完成后的分支状态**。如果该次 push 恰好包含新增的 workflow 文件，该 push 本身就会触发新 workflow（GitHub 会在 push 后扫描 `.github/workflows/` 目录）。

**实测验证**（2026-08-19，fork 环境）：

| 事件 | 触发方式 | 结果 |
|------|---------|------|
| 第一次 push ReleaseWorkflow.yml 到 fork main | `push` 自动触发 | ✅ 自动执行，创建了 tag + Release |
| 第二次 push（版本改为 0.8.9） | `push` 自动触发 | ✅ 自动执行，创建了 V0.8.9 tag + Release |
| dry-run 验证 | `workflow_dispatch` | ✅ 幂等保护正确跳过已存在 tag |

**注意**：`workflow_dispatch` 手动触发需要 workflow 已存在于默认分支（GitHub 平台限制），因此**首次合并前无法在 UI 手动触发**——但 `push` 自动触发不受此限制，这正是 release 流程依赖 `push` 事件的原因。

---

## 4. 手动兜底方案

如遇 workflow 未触发或需要强制操作，可使用以下命令兜底：

```bash
# 手动打 tag（annotated）
git tag -a V0.8.9 -m "Release V0.8.9"
git push origin V0.8.9

# 手动创建 Release
gh release create V0.8.9 --generate-notes --repo ACANX/MetaOpen
```

---

## 5. 相关 Workflow 一览

| Workflow | 触发方式 | 职责 |
|----------|---------|------|
| `UpdateProjectVersion.yml` | 手动 | 发版改号（revision + 各子版本） |
| `UpdateBOMAIODeps.yml` | 手动/每日 | 同步 bom-aio 依赖 |
| `UpdateBomGraalvmVersion.yml` | 手动/每日 | bom-graalvm 版本检查（派生模式下仅提示） |
| `ReleaseFullArtifactsByBatch.yaml` | 手动 | 发布 Maven 制品到 Central |
| **`ReleaseWorkflow.yml`** | **push main** | **自动打 tag + 创建 GitHub Release** |

---

## 6. 验证记录

### 6.1 ReleaseWorkflow 实验验证（fork: abcnx/MetaOpen）

| 验证项 | 结果 |
|--------|------|
| 版本提取（pom.xml revision → V<version>） | ✅ 0.8.8 → V0.8.8，0.8.9 → V0.8.9 |
| annotated tag 创建 | ✅ `git tag -a` 正确（含 tagger 对象） |
| GitHub Release 创建 | ✅ 自动生成 changelog（V0.8.8...V0.8.9） |
| 幂等保护 | ✅ tag 已存在时跳过，不报错 |
| push 首次触发 | ✅ workflow 文件随 push 进入 main 时自动触发 |
| 实验清理 | ✅ 测试 tag/Release 已删除，fork 恢复原状 |

### 6.2 版本对齐验证（UpdateProjectVersion + bom-graalvm）

| 验证项 | 结果 |
|--------|------|
| 模拟发版 0.8.10 | ✅ revision → 0.8.10，bom-graalvm.version → 25.0.8.10 |
| UpdateBOMAIODeps 特性分支验证 | ✅ graalvm 保持 1.1.9，无降级 |

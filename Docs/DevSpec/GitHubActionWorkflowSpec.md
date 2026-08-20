# GitHub Action 工作流编写规范（GitHubActionWorkflowSpec）

> 适用范围：MetaOpen 仓库 `.github/workflows/` 目录下的所有 GitHub Actions 工作流文件（`.yml` / `.yaml`）。
> 最后更新：2026-08-19

---

## 1. 文件命名规范

- **工作流文件名使用大驼峰（PascalCase）命名**，不使用连字符（`-`）、下划线（`_`）或空格。
- **文件名必须与工作流文件顶部的 `name` 属性值保持一致**（同名），便于文件与工作流的双向定位。
  - 正确示例：文件 `ReleaseWorkflow.yml` ↔ `name: ReleaseWorkflow`
  - 正确示例：文件 `UpdateBOMAIODeps.yml` ↔ `name: UpdateBOMAIODeps`
  - 错误示例：文件 `Foo.yml` 内部 `name: Bar` ❌（文件名与 name 不一致）
- 正确示例：
  - `ReleaseWorkflow.yml`
  - `UpdateBOMAIODeps.yml`
  - `MultiMavenJDKBranchCI.yml`
  - `SonarCloudCodeAnalysis.yml`
- 错误示例：
  - `release-workflow.yml` ❌
  - `update_bom_deps.yml` ❌
  - `my workflow.yml` ❌
- 扩展名统一使用 `.yml`（当前仓库存在 `.yaml` 历史文件，新文件一律使用 `.yml`）。
- 重命名工作流时，文件名与 `name` 属性需同步修改。

## 2. 工作流顶层 `name` 规范

- 工作流顶层 `name` 使用大驼峰命名，**不含空格**。
- 正确示例：`name: ReleaseWorkflow`
- 错误示例：`name: Release Workflow` ❌

## 3. Job / Step 的 `name` 规范

- **Job 的 `name`、Step 的 `name` 均使用大驼峰命名，中间不使用带空格的 name**。
- 正确示例：
  ```yaml
  jobs:
    release:
      runs-on: ubuntu-latest
      steps:
        - name: CheckoutCode          # ✅ 大驼峰、无空格
        - name: ExtractVersionFromPom # ✅ 大驼峰、无空格
        - name: CreateAndPushTag      # ✅ 大驼峰、无空格
  ```
- 错误示例：
  ```yaml
  steps:
    - name: Checkout repository       # ❌ 含空格
    - name: Build with Maven          # ❌ 含空格
    - name: Setup Java                # ❌ 含空格
  ```

### 命名要点

| 原则 | 说明 |
|------|------|
| 大驼峰 | 每个单词首字母大写，其余小写，如 `CreateGitHubRelease` |
| 无空格 | name 中不出现空格、连字符、下划线 |
| 语义清晰 | 名称应能概括步骤用途，如 `CheckIfTagExists`、`DryRunVerify` |
| 动词开头 | 步骤名以动词开头（Check / Create / Setup / Build / Publish 等） |

## 4. Step `id` 规范

- Step 的 `id` 使用**小驼峰（camelCase）**命名，与 `name` 风格区分。
- 正确示例：
  ```yaml
  - name: ExtractVersionFromPom
    id: version
  - name: CheckIfTagExists
    id: tag-check
  ```
- `id` 用于后续步骤引用（`steps.<id>.outputs.xxx`），命名应简洁且唯一。

## 5. 参考示例

`.github/workflows/ReleaseWorkflow.yml` 是符合本规范的推荐参考示例：

```yaml
name: ReleaseWorkflow                      # ✅ 顶层 name 大驼峰无空格

on:
  push:
    branches: [main]
  workflow_dispatch:
    inputs:
      dry_run:
        description: 'Dry run 模式：仅验证逻辑，不实际创建 tag 和 GitHub Release'
        type: boolean
        default: false

permissions:
  contents: write

jobs:
  release:                                 # ✅ job id 小驼峰
    runs-on: ubuntu-latest
    steps:
      - name: CheckoutCode                 # ✅ step name 大驼峰无空格
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: ExtractVersionFromPom        # ✅
        id: version
        run: |
          VERSION=$(grep -oP '(?<=<revision>)[^<]+' pom.xml | head -1 | tr -d ' \r\n')
          TAG="V${VERSION}"
          echo "version=${VERSION}" >> "$GITHUB_OUTPUT"
          echo "tag=${TAG}" >> "$GITHUB_OUTPUT"

      - name: CheckIfTagExists             # ✅
        id: tag-check
        run: |
          TAG="${{ steps.version.outputs.tag }}"
          if git rev-parse "$TAG" >/dev/null 2>&1; then
            echo "exists=true" >> "$GITHUB_OUTPUT"
          else
            echo "exists=false" >> "$GITHUB_OUTPUT"
          fi

      - name: DryRunVerify                 # ✅
        if: github.event.inputs.dry_run == 'true'
        run: echo "DRY RUN 验证模式"

      - name: CreateAndPushTag             # ✅
        if: steps.tag-check.outputs.exists == 'false' && github.event.inputs.dry_run != 'true'
        run: |
          git config user.name "github-actions[bot]"
          git config user.email "github-actions[bot]@users.noreply.github.com"
          git tag -a "${{ steps.version.outputs.tag }}" -m "Release ${{ steps.version.outputs.tag }}"
          git push origin "${{ steps.version.outputs.tag }}"

      - name: CreateGitHubRelease          # ✅
        if: steps.tag-check.outputs.exists == 'false' && github.event.inputs.dry_run != 'true'
        uses: softprops/action-gh-release@v2
        with:
          tag_name: ${{ steps.version.outputs.tag }}
          name: ${{ steps.version.outputs.tag }}
          generate_release_notes: true
```

## 6. 其他规范要点

| 项目 | 规范 |
|------|------|
| 触发方式 | `on:` 使用仓库内既有惯例；`workflow_dispatch` 的 `inputs` 命名用小驼峰（如 `dry_run`） |
| 权限 | 显式声明 `permissions:`，遵循最小权限原则（如 `contents: write`） |
| secrets | 使用 `${{ secrets.XXX }}`，禁止明文密钥 |
| shell 脚本 | 多行命令用 `run: \|` 块，保持可读性 |
| 条件执行 | 使用 `if:` 条件（如 `steps.xxx.outputs.xxx == 'true'`），避免整段脚本内判断 |
| 中文注释 | 关键步骤可加中文注释，与仓库交流语言（简体中文）一致 |
| 第三方 Action 引用 | **固定完整 commit SHA**（如 `crazy-max/ghaction-import-gpg@1c6a9e...`），不使用 tag（`@v3`）；tag 可被移动/删除，SHA 不可变（GitHub 官方安全加固建议，SonarCloud S7637）。版本维护由 Dependabot（github-actions 生态）自动更新 SHA |
| 官方 Action 引用 | `actions/*`、`github/*` 命名空间可继续用 tag（如 `actions/checkout@v5`），由 GitHub 官方维护 |

## 7. 检查清单

- [ ] 文件名大驼峰、`.yml` 扩展名
- [ ] **文件名与顶层 `name` 值一致**（同名）
- [ ] 顶层 `name` 大驼峰无空格
- [ ] 所有 job / step 的 `name` 大驼峰无空格
- [ ] step `id` 小驼峰且唯一
- [ ] 显式 `permissions`，最小权限
- [ ] 无明文密钥

## 8. 存量文件处理

仓库现有部分 workflow（如 `CodeQLAdvanced.yml`）的 step name 仍使用带空格写法。**存量文件不强制立即整改**，但满足以下条件之一时应同步改造为符合本规范：
- 对存量 workflow 进行功能性修改时
- 新增 step / job 时
- 涉及发布、版本管理的核心 workflow（`ReleaseWorkflow.yml`、`UpdateProjectVersion.yml` 等）

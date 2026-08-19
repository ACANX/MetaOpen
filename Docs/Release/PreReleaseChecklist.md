# MetaOpen 正式发版前检查清单（Pre-Release Checklist）

> 适用于每次正式版本（如 0.8.9）发布前执行。按顺序逐项确认，全部通过后方可发起 dev→main 合并。
>
> 关联文档：[README.md](./README.md)（发布方案总览）、[PostReleaseChecklist.md](./PostReleaseChecklist.md)（发版后收尾）
> 最后更新：2026-08-19

---

## 1. 版本号检查

- [ ] 根 `pom.xml` 的 `<revision>` 为待发布版本（如 `0.8.9`）
- [ ] `version` 文件内容与 `<revision>` 一致
- [ ] `meta-open.version` / `meta.version` 等子版本属性已同步（`UpdateProjectVersion` 已执行）
- [ ] `bom-aio-origin/pom.xml` 的 `bom-graalvm.version` 为 `25.<revision>`（如 `25.0.8.9`）
- [ ] `meta-bom/pom.xml` 的 `bom-graalvm.version` 与上述一致
- [ ] 全仓库搜索无旧版本号残留（如发布 0.8.9 时无遗漏的 0.8.8 引用）

## 2. 分支与 PR 状态

- [ ] 目标发布分支（dev）无未合并的 open PR（或已确认可推迟）
- [ ] dev 分支领先 main 的提交均为预期内容（`git log origin/main..origin/dev --oneline`）
- [ ] ReleaseWorkflow（`.github/workflows/ReleaseWorkflow.yml`）已存在于 dev 分支
- [ ] 发布方案文档（Docs/Release/）已更新至当前版本

## 3. CI 与构建验证

- [ ] `MultiMavenJDKBranchCI` 在 dev 最新提交上 **success**（JDK17/21/25 × 平台矩阵）
- [ ] `SonarQube`（自建）分析 **success**
- [ ] `CodeQLAdvanced` 安全扫描 **success**
- [ ] `UpdateBOMAIODeps` 执行后无异常（bom-aio 依赖同步正常，无版本降级）
- [ ] `Automatic Dependency Submission` **success**
- [ ] SonarCloudCodeAnalysis：确认失败原因已知（当前为 SONAR_TOKEN 授权问题，见 issue #2507）；若为质量门禁硬性要求，需先修复

## 4. 依赖与安全

- [ ] Dependabot 安全警报中，目标版本（main 合并后）对应的依赖均为修复版本
      （当前涉及：httpclient5 ≥5.6.3、netty ≥4.2.16.Final、jsoup ≥1.23.1）
- [ ] 无未处理的 CRITICAL/HIGH 级别安全警报

## 5. 发布流程准备

- [ ] 已确认 ReleaseWorkflow 触发方式（push 到 main 自动触发，首次合并即可触发，见 [WorkflowTriggerAnalysis.md](./WorkflowTriggerAnalysis.md)）
- [ ] 已确认 main 分支无同名 tag（如 `V0.8.9`）已存在
- [ ] （可选）已准备 Release 备注要点 / 已知变更清单
- [ ] （可选）已确认 Maven Central 发布凭据（OSSRH/GPG）有效，如需本次发布制品

## 6. 最终确认

- [ ] 以上全部通过
- [ ] 已获得发布决策人（ACANX）放行确认

---

## 执行入口

全部检查通过后，执行发布：

```bash
# dev → main 合并 PR
gh pr create --repo ACANX/MetaOpen --base main --head dev \
  --title "Release 0.8.9: sync dev → main" \
  --body "正式发布 0.8.9，合并后 ReleaseWorkflow 自动打 tag 并创建 Release"

# 合并后 → 转 PostReleaseChecklist.md 执行收尾
```

> 提示：合并后到 Actions 页面确认 ReleaseWorkflow 已自动触发；若未触发，参见 [WorkflowTriggerAnalysis.md](./WorkflowTriggerAnalysis.md) 第 5.3 节手动兜底。

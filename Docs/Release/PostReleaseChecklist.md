# MetaOpen 发版后收尾/后置工作清单（Post-Release Checklist）

> 适用于正式版本发布（dev→main 合并 + ReleaseWorkflow 触发）完成后执行，确认发布结果并处理后续事项。
>
> 关联文档：[README.md](./README.md)（发布方案总览）、[PreReleaseChecklist.md](./PreReleaseChecklist.md)（发版前检查）
> 最后更新：2026-08-19

---

## 1. 发布结果确认

- [ ] `ReleaseWorkflow` 已触发且 **success**（Actions 页面确认 `event=push`）
- [ ] Tag 已创建：`git ls-remote --tags origin | grep V<version>`（如 `V0.8.9`，annotated tag）
- [ ] GitHub Release 已创建：`gh release list --repo ACANX/MetaOpen`
- [ ] Release 内容正确（版本号、changelog 自动生成、无错误资产）

## 2. Maven Central 发布（可选但通常需要）

- [ ] 触发 `ReleaseFullArtifactsByBatch` workflow（手动）
- [ ] 确认各模块 deploy **success**（os-dependencies / meta-model / meta-bom 等）
- [ ] 验证 Central 制品可用：
      `curl -s "https://repo1.maven.org/maven2/com/acanx/meta/<artifact>/<version>/" | grep <version>`
- [ ] 确认 bom-graalvm `25.<version>` 已发布（供外部依赖解析）
- [ ] 确认 bom-aio `<version>` 已发布且为自包含展开版（无 import 依赖）

## 3. 安全警报与依赖

- [ ] Dependabot 安全警报随 main 更新自动关闭（指向旧版本的警报应变为 fixed）
- [ ] 如警报未自动关闭，检查是否仍指向旧版本（可能需要手动确认/重扫）

## 4. 分支与后续版本准备

- [ ] 确认 main 已包含发布版本的全部代码（`git log origin/main --oneline -3`）
- [ ] 如发布流程包含版本递增，确认下一版本号规划（如 0.8.10 或 0.9.0）
- [ ] （可选）dev 分支已准备好继续开发（新特性分支基于最新 dev）

## 5. 文档与记录

- [ ] `Docs/Release/README.md` 的「版本演进历史」已更新（新增本次版本行）
- [ ] 发布相关 ISSUE 已更新状态（如 SonarCloud 问题 #2507 若有进展）
- [ ] （可选）在发布群/通知渠道同步发布结果

## 6. 已知遗留事项跟踪

- [ ] SonarCloudCodeAnalysis 失败问题（issue #2507）是否已解决；如未解决，确认不阻塞
- [ ] 记录本次发布中发现的任何新问题到 ISSUE 跟踪

---

## 异常处理速查

| 场景 | 处理方式 |
|------|---------|
| ReleaseWorkflow 未触发 | 检查 Actions 页面；手动触发：`gh workflow run ReleaseWorkflow.yml --repo ACANX/MetaOpen --ref main` |
| Tag 已存在（幂等跳过） | 确认 tag 指向正确提交；如需重建：删除后重新触发 |
| Release 内容错误 | `gh release edit V<version> --repo ACANX/MetaOpen` 修正 |
| Maven 发布失败 | 查看 workflow 日志；常见为凭据/GPG 问题；修复后重新触发 |
| 安全警报未关闭 | 检查依赖版本是否真的修复；必要时手动 dismiss 并注明原因 |

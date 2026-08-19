# Docs 文档中心

> MetaOpen 项目文档索引。各文档按主题组织，开发相关文档位于 `Docs/Dev/`，发布相关文档位于 `Docs/Release/`。

## 文档目录结构

```
Docs/
├── README.md                        # 本文档（文档中心索引）
├── Dev/
│   └── Introduction/                # 开发入门指南
│       ├── BomDependencyAnalysis.md # BOM 依赖版本分析
│       ├── IncrementalDeploy.md     # 增量发布（mvn deploy -pl）
│       └── NewArtifactGuide.md      # 如何生成新的 Artifact（archetype）
└── Release/                         # 发布相关文档
    ├── README.md                    # 发布方案总览
    ├── PreRelease-Checklist.md      # 发版前检查清单
    ├── PostRelease-Checklist.md     # 发版后收尾清单
    └── WorkflowTrigger-Analysis.md  # 首次合并触发 ReleaseWorkflow 问题探究
```

## 开发入门（Docs/Dev/Introduction/）

| 文档 | 内容 | 适用场景 |
|------|------|---------|
| [NewArtifactGuide.md](./Dev/Introduction/NewArtifactGuide.md) | 使用 `tool-archetype` 生成新模块的命令示例与步骤 | 创建新的 model-*/sdk-*/api-* 模块 |
| [IncrementalDeploy.md](./Dev/Introduction/IncrementalDeploy.md) | `mvn deploy -pl` 增量发布用法与注意事项 | 局部模块快速发布 |
| [BomDependencyAnalysis.md](./Dev/Introduction/BomDependencyAnalysis.md) | `mvn help:effective-pom` 分析 BOM 依赖版本 | 排查依赖版本冲突、验证 BOM 导入结果 |

## 发布流程（Docs/Release/）

| 文档 | 内容 | 适用场景 |
|------|------|---------|
| [README.md](./Release/README.md) | 发布方案总览（版本管理、ReleaseWorkflow、发布流程） | 了解发布整体机制 |
| [PreRelease-Checklist.md](./Release/PreRelease-Checklist.md) | 发版前检查工作清单（6 大类） | 每次正式发版前逐项确认 |
| [PostRelease-Checklist.md](./Release/PostRelease-Checklist.md) | 发版后收尾/后置工作清单（含异常速查） | 发布完成后执行 |
| [WorkflowTrigger-Analysis.md](./Release/WorkflowTrigger-Analysis.md) | 首次合并触发 ReleaseWorkflow 问题探究 | 理解 push 触发机制与边界情况 |

## 其他参考

- 根目录 [README.md](../README.md)：项目总览与编译说明
- [AGENTS.md](../AGENTS.md)：仓库开发规范（语言、命名、提交规范等）
- [SECURITY.md](../SECURITY.md)：安全说明

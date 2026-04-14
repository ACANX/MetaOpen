#!/usr/bin/env python3
"""
Update the <dependencyManagement><dependencies> section of a target pom.xml
with all <dependency> entries extracted from an effective-pom.xml.
"""

import argparse
import sys
from lxml import etree

NAMESPACE = "http://maven.apache.org/POM/4.0.0"
NSMAP = {"p": NAMESPACE}

def parse_dependencies(effective_pom_path):
    """Extract all <dependency> elements under <dependencyManagement><dependencies>."""
    tree = etree.parse(effective_pom_path)
    root = tree.getroot()

    dep_mgmt = root.find("./p:dependencyManagement", namespaces=NSMAP)
    if dep_mgmt is None:
        sys.exit("No <dependencyManagement> section found in effective POM.")

    deps_elem = dep_mgmt.find("./p:dependencies", namespaces=NSMAP)
    if deps_elem is None:
        sys.exit("No <dependencies> inside <dependencyManagement> found.")

    dependencies = deps_elem.findall("./p:dependency", namespaces=NSMAP)
    # 深拷贝每个 dependency 元素
    return [etree.fromstring(etree.tostring(dep)) for dep in dependencies]

def print_dependency_gav(dependencies):
    """Print each dependency's GAV (groupId:artifactId:version) to GitHub Actions log."""
    print("=======================================")
    print(f"解析到 {len(dependencies)} 个依赖构件，清单如下：")
    print("=======================================")
    for dep in dependencies:
        group_id = dep.findtext("./p:groupId", namespaces=NSMAP) or ""
        artifact_id = dep.findtext("./p:artifactId", namespaces=NSMAP) or ""
        version = dep.findtext("./p:version", namespaces=NSMAP) or ""
        # 若 version 未定义（极少见），显示 "unknown"
        if not version:
            version = "unknown"
        print(f"依赖构件  {group_id}:{artifact_id}:{version}")

def update_target_pom(target_pom_path, new_dependencies):
    """Replace the existing <dependencies> children with new_dependencies in target POM."""
    # 保留原始空白文本，以便手动控制换行缩进
    parser = etree.XMLParser(remove_blank_text=False)
    tree = etree.parse(target_pom_path, parser)
    root = tree.getroot()

    # 定位或创建 <dependencyManagement>
    dep_mgmt = root.find("./p:dependencyManagement", namespaces=NSMAP)
    if dep_mgmt is None:
        dep_mgmt = etree.SubElement(root, "{{{0}}}dependencyManagement".format(NAMESPACE))

    # 定位或创建 <dependencies>
    deps_elem = dep_mgmt.find("./p:dependencies", namespaces=NSMAP)
    if deps_elem is None:
        deps_elem = etree.SubElement(dep_mgmt, "{{{0}}}dependencies".format(NAMESPACE))
        # 为新元素设置文本，使第一个子元素从下一行开始并缩进
        deps_elem.text = "\n        "
    else:
        # 清空现有子元素
        for child in deps_elem:
            deps_elem.remove(child)
        # 同样设置文本，保证格式
        deps_elem.text = "\n        "

    # 添加所有新依赖，并手动在每个依赖后添加换行符
    for i, dep in enumerate(new_dependencies):
        deps_elem.append(dep)
        # 除最后一个依赖外，每个依赖的尾随文本为换行+缩进
        if i < len(new_dependencies) - 1:
            dep.tail = "\n        "
        else:
            # 最后一个依赖的尾随文本换行并缩进到与 </dependencies> 对齐
            dep.tail = "\n    "

    # 处理没有依赖的情况
    if len(new_dependencies) == 0:
        deps_elem.text = "\n    "

    # 写入文件，关闭 pretty_print，因为我们已完全控制空白
    tree.write(
        target_pom_path,
        encoding="utf-8",
        xml_declaration=True,
        pretty_print=False
    )
    print(f"Updated {target_pom_path} with {len(new_dependencies)} dependencies.")

def main():
    parser = argparse.ArgumentParser(description="Update BOM dependencies from effective POM")
    parser.add_argument("--effective-pom", required=True, help="Path to effective-pom.xml")
    parser.add_argument("--target-pom", required=True, help="Path to target pom.xml")
    args = parser.parse_args()

    deps = parse_dependencies(args.effective_pom)

    # ✅ 新增：打印依赖清单到 GitHub Actions 日志
    print_dependency_gav(deps)

    update_target_pom(args.target_pom, deps)

if __name__ == "__main__":
    main()

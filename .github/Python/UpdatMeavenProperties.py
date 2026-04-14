#!/usr/bin/env python3
"""
Update specified Maven properties in a pom.xml file using targeted regex replacement.
Preserves all comments, formatting, and whitespace.
"""

import argparse
import re
import sys

def update_properties(pom_path, new_version, properties):
    """Replace property values inside the <properties> block only."""
    with open(pom_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # 定位 <properties> 块的起止位置
    start = content.find('<properties>')
    end = content.find('</properties>', start)
    if start == -1 or end == -1:
        print(f"⚠️  No <properties> section found in {pom_path}")
        return

    # 将文件内容拆分为三部分：之前、properties 块、之后
    before = content[:start]
    prop_block = content[start:end + len('</properties>')]
    after = content[end + len('</properties>'):]

    updated = False
    for prop in properties:
        # 匹配 <prop>旧值</prop>，其中旧值不含 '<' 字符（避免嵌套标签干扰）
        pattern = re.compile(rf'(<{re.escape(prop)}>)[^<]*(</{re.escape(prop)}>)')
        new_block, count = pattern.subn(rf'\g<1>{new_version}\g<2>', prop_block)
        if count > 0:
            prop_block = new_block
            print(f"  ✅ Updated {prop} = {new_version}")
            updated = True
        else:
            print(f"  ⚠️  Property '{prop}' not found in properties section, skipping.")

    if updated:
        new_content = before + prop_block + after
        with open(pom_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"📄 Updated {pom_path}")
    else:
        print(f"ℹ️  No properties updated in {pom_path}")

def main():
    parser = argparse.ArgumentParser(description="Update Maven properties in pom.xml")
    parser.add_argument('--pom-file', required=True, help='Path to pom.xml')
    parser.add_argument('--new-version', required=True, help='New version string')
    parser.add_argument('--properties', nargs='+', required=True,
                        help='List of property names to update (e.g., revision meta.version)')
    args = parser.parse_args()

    update_properties(args.pom_file, args.new_version, args.properties)

if __name__ == '__main__':
    main()

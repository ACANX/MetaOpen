#!/usr/bin/env python3
"""
Update specified Maven properties in a pom.xml file.
Usage: python UpdatMeavenProperties.py --pom-file pom.xml --new-version 1.2.3 --properties revision meta.version
"""

import argparse
import sys
import xml.etree.ElementTree as ET

NAMESPACE = "http://maven.apache.org/POM/4.0.0"

def update_properties(pom_path, new_version, properties):
    """Update given properties in pom.xml to new_version."""
    # Register namespace to avoid ns0: prefix in output
    ET.register_namespace('', NAMESPACE)
    ns = {'p': NAMESPACE}

    try:
        tree = ET.parse(pom_path)
        root = tree.getroot()
    except Exception as e:
        print(f"Error parsing {pom_path}: {e}", file=sys.stderr)
        sys.exit(1)

    # Find or create <properties> element
    props_elem = root.find('./p:properties', ns)
    if props_elem is None:
        # Create properties element if missing
        props_elem = ET.SubElement(root, f'{{{NAMESPACE}}}properties')
        # Add proper indentation (optional but nice)
        props_elem.text = "\n    "
        props_elem.tail = "\n"

    updated = False
    for prop_name in properties:
        elem = props_elem.find(f'./p:{prop_name}', ns)
        if elem is not None:
            elem.text = new_version
            print(f"  ✅ Updated {prop_name} = {new_version}")
            updated = True
        else:
            print(f"  ⚠️  Property '{prop_name}' not found in {pom_path}, skipping.")

    if updated:
        try:
            tree.write(pom_path, encoding='utf-8', xml_declaration=True)
            print(f"📄 Updated {pom_path}")
        except Exception as e:
            print(f"Error writing {pom_path}: {e}", file=sys.stderr)
            sys.exit(1)
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

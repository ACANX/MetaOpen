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

    # Locate /project/dependencyManagement/dependencies
    dep_mgmt = root.find("./p:dependencyManagement", namespaces=NSMAP)
    if dep_mgmt is None:
        sys.exit("No <dependencyManagement> section found in effective POM.")

    deps_elem = dep_mgmt.find("./p:dependencies", namespaces=NSMAP)
    if deps_elem is None:
        sys.exit("No <dependencies> inside <dependencyManagement> found.")

    # Return a list of <dependency> elements (deep copies)
    dependencies = deps_elem.findall("./p:dependency", namespaces=NSMAP)
    return [etree.fromstring(etree.tostring(dep)) for dep in dependencies]

def update_target_pom(target_pom_path, new_dependencies):
    """Replace the existing <dependencies> children with new_dependencies in target POM."""
    parser = etree.XMLParser(remove_blank_text=True)
    tree = etree.parse(target_pom_path, parser)
    root = tree.getroot()

    # Find or create <dependencyManagement>
    dep_mgmt = root.find("./p:dependencyManagement", namespaces=NSMAP)
    if dep_mgmt is None:
        dep_mgmt = etree.SubElement(root, "{{{0}}}dependencyManagement".format(NAMESPACE))

    # Find or create <dependencies>
    deps_elem = dep_mgmt.find("./p:dependencies", namespaces=NSMAP)
    if deps_elem is None:
        deps_elem = etree.SubElement(dep_mgmt, "{{{0}}}dependencies".format(NAMESPACE))
    else:
        # Clear existing children
        for child in deps_elem:
            deps_elem.remove(child)

    # Append all new dependencies
    for dep in new_dependencies:
        deps_elem.append(dep)

    # Write back with pretty formatting and XML declaration
    tree.write(
        target_pom_path,
        encoding="utf-8",
        xml_declaration=True,
        pretty_print=True
    )
    print(f"Updated {target_pom_path} with {len(new_dependencies)} dependencies.")

def main():
    parser = argparse.ArgumentParser(description="Update BOM dependencies from effective POM")
    parser.add_argument("--effective-pom", required=True, help="Path to effective-pom.xml")
    parser.add_argument("--target-pom", required=True, help="Path to target pom.xml")
    args = parser.parse_args()

    deps = parse_dependencies(args.effective_pom)
    update_target_pom(args.target_pom, deps)

if __name__ == "__main__":
    main()

package com.acanx.meta.model.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DuplicateClassCheck
 *
 * @author ACANX
 * @since 20250901
 */
public class DuplicateClassCheck {
    // 存储类名与对应文件路径的映射
    private static Map<String, List<String>> classMap = new HashMap<>();

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("请提供Maven项目的根目录路径作为参数");
//            System.out.println("用法: java DuplicateClassDetector <项目根目录>");
//            return;
//        }
        String projectRoot = "D:/Code/JavaCode/MetaOpen";
        System.out.println("开始扫描项目: " + projectRoot);
        try {
            // 扫描项目中的所有类文件
            scanProject(projectRoot);
            // 检测并报告重复的类
            detectAndReportDuplicates();
        } catch (IOException e) {
            System.err.println("扫描过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 扫描项目中的所有类文件
     */
    private static void scanProject(String projectRoot) throws IOException {
        Path startPath = Paths.get(projectRoot);
        System.out.println(startPath.toAbsolutePath().toString());
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (isClassFile(file)) {
                    System.out.println(file.toAbsolutePath().toString());
                    processClassFile(file, startPath);
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("无法访问文件: " + file + ": " + exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 判断文件是否为类文件
     */
    private static boolean isClassFile(Path file) {
        String fileName = file.toString();
        return fileName.endsWith(".class") || fileName.endsWith(".java");
    }

    /**
     * 处理类文件，提取类名并记录到映射中
     */
    private static void processClassFile(Path file, Path projectRoot) {
        try {
            // 获取相对于项目根目录的路径
            Path relativePath = projectRoot.relativize(file);

            // 根据文件类型提取类名
            String className = extractClassName(file, relativePath);
            System.out.println(className);
            if (className != null && !className.isEmpty()) {
                // 将类名和文件路径添加到映射中
                if (!classMap.containsKey(className)) {
                    classMap.put(className, new ArrayList<>());
                }
                classMap.get(className).add(relativePath.toString());
            }
        } catch (Exception e) {
            System.err.println("处理文件时出错: " + file + ": " + e.getMessage());
        }
    }

    /**
     * 从文件路径中提取类名
     */
    private static String extractClassName(Path file, Path relativePath) {
        String fileName = file.toString();
        if (fileName.endsWith(".class")) {
            // 对于.class文件，从文件路径推断类名
            return extractClassNameFromClassFile(relativePath);
        } else
            if (fileName.endsWith(".java")) {
            // 对于.java文件，从文件路径推断类名
            return extractClassNameFromJavaFile(relativePath);
        }
        System.out.println(file.toAbsolutePath().toString());
        return null;
    }

    /**
     * 从.class文件路径提取类名
     */
    private static String extractClassNameFromClassFile(Path relativePath) {
        String pathStr = relativePath.toString();

        // 移除开头的target/classes/或target/test-classes/
        if (pathStr.indexOf("target\\classes\\") >=0 ) {
            pathStr = pathStr.substring(pathStr.indexOf("target\\classes\\")+15);
            // System.out.println(pathStr);
        } else if (pathStr.indexOf("target\\test-classes\\") >=0 ) {
            pathStr = pathStr.substring(pathStr.indexOf("target\\test-classes\\")+15);
            // System.out.println(pathStr);
        }

        // 将路径分隔符替换为.，并移除.class后缀
        return pathStr.replace(File.separatorChar, '.')
                .replace(".class", "");
    }

    /**
     * 从.java文件路径提取类名
     */
    private static String extractClassNameFromJavaFile(Path relativePath) {
        String pathStr = relativePath.toString();
        // System.out.println("pathStr: " + pathStr);
        // 移除开头的src/main/java/或src/test/java/
        if (pathStr.indexOf("src\\main\\java\\") >=0 ) {
            pathStr = pathStr.substring(pathStr.indexOf("src\\main\\java\\")+14);
            // System.out.println(pathStr);
        } else if (pathStr.indexOf("src\\test\\java\\") >=0 ) {
            pathStr = pathStr.substring(pathStr.indexOf("src\\test\\java\\")+14);
            // System.out.println(pathStr);
        } else {
            System.out.println("=============" + pathStr);
        }
        // 将路径分隔符替换为.，并移除.java后缀
        return pathStr.replace(File.separatorChar, '.').replace(".java", "");
    }

    /**
     * 检测并报告重复的类
     */
    private static void detectAndReportDuplicates() {
        boolean hasDuplicates = false;

        for (Map.Entry<String, List<String>> entry : classMap.entrySet()) {
            String className = entry.getKey();
            List<String> filePaths = entry.getValue();

            if (filePaths.size() > 1) {
                hasDuplicates = true;
                System.out.println("\n发现重复类: " + className);
                System.out.println("出现位置:");

                for (String path : filePaths) {
                    System.out.println("  - " + path);
                }
            }
        }

        if (!hasDuplicates) {
            System.out.println("未发现重复类");
        } else {
            System.out.println("\n扫描完成，发现重复类!");
        }
    }
}

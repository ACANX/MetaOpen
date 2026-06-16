package com.acanx.meta.base.file.mvsv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MVSV 文件解析器
 *
 * MVSV（Metadata Vertical bar Separated Values）格式文件的解析器，
 * 用于将 MVSV 文件解析为结构化的 MVSVData 对象。
 *
 * @author ACANX
 * @since 2026-05-21
 */
public class MVSVParser {

    /**
     * 解析 MVSV 文件
     *
     * @param filePath 文件路径
     * @return 解析后的数据对象
     * @throws IOException 文件读取异常
     */
    public MVSVData parse(String filePath) throws IOException {
        List<String> lines = readFile(filePath);

        // 解析元数据区
        MVSVMetadata metadata = parseMetadata(lines);

        // 解析数据区
        List<List<String>> rows = parseMVSVData(lines);

        // 构建结果
        MVSVData data = new MVSVData();
        data.setMetadata(metadata);
        // 从元数据获取 headers，无元数据时 headers 为 null
        if (metadata.getField() != null && !metadata.getField().isEmpty()) {
            data.setHeaders(metadata.getFieldList());
        }
        data.setFieldNames(metadata.getFieldNameList());
        data.setFieldTypes(metadata.getFieldTypeList());
        data.setRows(rows);

        return data;
    }

    /**
     * 从字符串解析 MVSV 数据
     *
     * @param content MVSV 内容字符串
     * @return 解析后的数据对象
     */
    public MVSVData parseString(String content) {
        String[] lineArray = content.split("\n");
        List<String> lines = Arrays.asList(lineArray);

        // 解析元数据区
        MVSVMetadata metadata = parseMetadata(lines);

        // 解析数据区
        List<List<String>> rows = parseMVSVData(lines);

        // 构建结果
        MVSVData data = new MVSVData();
        data.setMetadata(metadata);
        // 从元数据获取 headers，无元数据时 headers 为 null
        if (metadata.getField() != null && !metadata.getField().isEmpty()) {
            data.setHeaders(metadata.getFieldList());
        }
        data.setFieldNames(metadata.getFieldNameList());
        data.setFieldTypes(metadata.getFieldTypeList());
        data.setRows(rows);

        return data;
    }

    /**
     * 读取文件所有行
     *
     * @param filePath 文件路径
     * @return 文件行列表
     * @throws IOException 文件读取异常
     */
    private List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 解析元数据区
     *
     * @param lines 文件行列表
     * @return 元数据对象
     */
    private MVSVMetadata parseMetadata(List<String> lines) {
        Map<String, String> metadataMap = new HashMap<>();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("#")) {
                break;
            }

            // 解析字段：# 字段名 : 字段值
            if (trimmedLine.contains(" : ")) {
                int colonIndex = trimmedLine.indexOf(" : ");
                String key = trimmedLine.substring(2, colonIndex);
                String value = trimmedLine.substring(colonIndex + 3);
                // 去除引号
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                metadataMap.put(key, value);
            }
        }

        MVSVMetadata metadata = new MVSVMetadata();
        metadata.setTitle(metadataMap.get("标题"));
        metadata.setTitleEn(metadataMap.get("Title"));
        metadata.setDataProvider(metadataMap.get("数据供应商"));
        metadata.setDataProviderEn(metadataMap.get("DataProvider"));
        metadata.setField(metadataMap.get("字段"));
        metadata.setFieldEn(metadataMap.get("Field"));
        metadata.setFieldName(metadataMap.get("字段名称"));
        metadata.setFieldNameEn(metadataMap.get("FieldName"));
        metadata.setFieldType(metadataMap.get("字段类型"));
        metadata.setFieldTypeEn(metadataMap.get("FieldType"));

        String countStr = getMetadataValue(metadataMap, "计数", "Count", "0");
        metadata.setCount(Integer.parseInt(countStr));

        metadata.setRemark(metadataMap.get("备注"));
        metadata.setRemarkEn(metadataMap.get("Remark"));
        metadata.setExtra(metadataMap);

        return metadata;
    }

    /**
     * 获取元数据值（支持中英文）
     *
     * @param map 元数据映射
     * @param zhKey 中文键
     * @param enKey 英文键
     * @param defaultValue 默认值
     * @return 元数据值
     */
    private String getMetadataValue(Map<String, String> map, String zhKey, String enKey, String defaultValue) {
        if (map.containsKey(zhKey)) {
            return map.get(zhKey);
        }
        if (map.containsKey(enKey)) {
            return map.get(enKey);
        }
        return defaultValue;
    }

    /**
     * 解析数据区
     *
     * @param lines 文件行列表
     * @return 数据行列表
     */
    private List<List<String>> parseMVSVData(List<String> lines) {
        List<List<String>> rows = new ArrayList<>();

        for (String line : lines) {
            String trimmedLine = line.trim();

            // 跳过注释行和空行
            if (trimmedLine.startsWith("#") || trimmedLine.isEmpty()) {
                continue;
            }

            // 数据行
            if (trimmedLine.contains("|")) {
                String[] values = trimmedLine.split("\\|", -1);
                rows.add(new ArrayList<>(Arrays.asList(values)));
            }
        }

        return rows;
    }
}
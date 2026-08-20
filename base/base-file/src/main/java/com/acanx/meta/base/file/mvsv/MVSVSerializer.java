package com.acanx.meta.base.file.mvsv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MVSV 文件序列化器
 *
 * MVSV（Metadata Vertical bar Separated Values）格式文件的序列化器，
 * 用于将 MVSVData 对象序列化为 MVSV 格式文件。
 *
 * @author ACANX
 * @since 2026-05-21
 */
public class MVSVSerializer {

    /**
     * 序列化为 MVSV 文件
     *
     * @param data 数据对象
     * @param filePath 输出文件路径
     * @throws IOException 文件写入异常
     */
    public void serialize(MVSVData data, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            // 写入元数据区
            writeMetadata(writer, data.getMetadata());

            // 写入空行分隔
            writer.newLine();

            // 写入数据区
            writeMVSVData(writer, data.getRows());
        }
    }

    /**
     * 序列化为字符串
     *
     * @param data 数据对象
     * @return MVSV 格式字符串
     */
    public String serializeToString(MVSVData data) {
        StringBuilder builder = new StringBuilder();

        // 写入元数据区
        writeMetadataToBuilder(builder, data.getMetadata());

        // 写入空行分隔
        builder.append('\n');

        // 写入数据区
        writeDataToBuilder(builder, data.getRows());

        return builder.toString();
    }

    /**
     * 写入元数据区
     *
     * @param writer 写入器
     * @param metadata 元数据
     * @throws IOException 写入异常
     */
    private void writeMetadata(BufferedWriter writer, MVSVMetadata metadata) throws IOException {
        // 中文元数据
        writeMetadataField(writer, "# 标题 : \"%s\"", metadata.getTitle());
        writeMetadataField(writer, "# 数据供应商 : %s", metadata.getDataProvider());
        writeMetadataField(writer, "# 字段 : %s", metadata.getField());
        writeMetadataField(writer, "# 字段名称 : %s", metadata.getFieldName());
        writeMetadataField(writer, "# 字段类型 : %s", metadata.getFieldType());
        writer.write(String.format("# 计数 : %d", metadata.getCount()));
        writer.newLine();
        writeMetadataField(writer, "# 备注 : \"%s\"", metadata.getRemark());

        // 英文元数据
        writeMetadataField(writer, "# Title : \"%s\"", metadata.getTitleEn());
        writeMetadataField(writer, "# DataProvider : %s", metadata.getDataProviderEn());
        writeMetadataField(writer, "# Field : %s", metadata.getFieldEn());
        writeMetadataField(writer, "# FieldName : %s", metadata.getFieldNameEn());
        writeMetadataField(writer, "# FieldType : %s", metadata.getFieldTypeEn());
        writer.write(String.format("# Count : %d", metadata.getCount()));
        writer.newLine();
        writeMetadataField(writer, "# Remark : \"%s\"", metadata.getRemarkEn());
    }

    /**
     * 元数据字段非空时写入
     *
     * @param writer 写入器
     * @param format 输出格式（含占位符）
     * @param value 字段值
     * @throws IOException 写入异常
     */
    private void writeMetadataField(BufferedWriter writer, String format, String value) throws IOException {
        if (value != null && !value.isEmpty()) {
            writer.write(String.format(format, value));
            writer.newLine();
        }
    }

    /**
     * 写入元数据区到 Builder
     *
     * @param builder 字符串构建器
     * @param metadata 元数据
     */
    private void writeMetadataToBuilder(StringBuilder builder, MVSVMetadata metadata) {
        // 中文元数据
        appendMetadataField(builder, "# 标题 : \"%s\"%n", metadata.getTitle());
        appendMetadataField(builder, "# 数据供应商 : %s%n", metadata.getDataProvider());
        appendMetadataField(builder, "# 字段 : %s%n", metadata.getField());
        appendMetadataField(builder, "# 字段名称 : %s%n", metadata.getFieldName());
        appendMetadataField(builder, "# 字段类型 : %s%n", metadata.getFieldType());
        builder.append(String.format("# 计数 : %d%n", metadata.getCount()));
        appendMetadataField(builder, "# 备注 : \"%s\"%n", metadata.getRemark());

        // 英文元数据
        appendMetadataField(builder, "# Title : \"%s\"%n", metadata.getTitleEn());
        appendMetadataField(builder, "# DataProvider : %s%n", metadata.getDataProviderEn());
        appendMetadataField(builder, "# Field : %s%n", metadata.getFieldEn());
        appendMetadataField(builder, "# FieldName : %s%n", metadata.getFieldNameEn());
        appendMetadataField(builder, "# FieldType : %s%n", metadata.getFieldTypeEn());
        builder.append(String.format("# Count : %d%n", metadata.getCount()));
        appendMetadataField(builder, "# Remark : \"%s\"%n", metadata.getRemarkEn());
    }

    /**
     * 元数据字段非空时追加到 Builder
     *
     * @param builder 字符串构建器
     * @param format 输出格式（含占位符）
     * @param value 字段值
     */
    private void appendMetadataField(StringBuilder builder, String format, String value) {
        if (value != null && !value.isEmpty()) {
            builder.append(String.format(format, value));
        }
    }

    /**
     * 写入数据区
     *
     * @param writer 写入器
     * @param rows 数据行
     * @throws IOException 写入异常
     */
    private void writeMVSVData(BufferedWriter writer, List<List<String>> rows) throws IOException {
        for (List<String> row : rows) {
            writer.write(String.join("|", row));
            writer.newLine();
        }
    }

    /**
     * 写入数据区到 Builder
     *
     * @param builder 字符串构建器
     * @param rows 数据行
     */
    private void writeDataToBuilder(StringBuilder builder, List<List<String>> rows) {
        for (List<String> row : rows) {
            builder.append(String.join("|", row));
            builder.append('\n');
        }
    }
}
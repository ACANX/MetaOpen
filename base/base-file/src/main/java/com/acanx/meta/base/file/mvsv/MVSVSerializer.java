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
 * MVSV（MVSVMetadata Vertical bar Separated Values）格式文件的序列化器，
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
        if (metadata.getTitle() != null && !metadata.getTitle().isEmpty()) {
            writer.write(String.format("# 标题 : \"%s\"", metadata.getTitle()));
            writer.newLine();
        }
        if (metadata.getDataProvider() != null && !metadata.getDataProvider().isEmpty()) {
            writer.write(String.format("# 数据供应商 : %s", metadata.getDataProvider()));
            writer.newLine();
        }
        if (metadata.getField() != null && !metadata.getField().isEmpty()) {
            writer.write(String.format("# 字段 : %s", metadata.getField()));
            writer.newLine();
        }
        if (metadata.getFieldName() != null && !metadata.getFieldName().isEmpty()) {
            writer.write(String.format("# 字段名称 : %s", metadata.getFieldName()));
            writer.newLine();
        }
        if (metadata.getFieldType() != null && !metadata.getFieldType().isEmpty()) {
            writer.write(String.format("# 字段类型 : %s", metadata.getFieldType()));
            writer.newLine();
        }
        writer.write(String.format("# 计数 : %d", metadata.getCount()));
        writer.newLine();
        if (metadata.getRemark() != null && !metadata.getRemark().isEmpty()) {
            writer.write(String.format("# 备注 : \"%s\"", metadata.getRemark()));
            writer.newLine();
        }

        // 英文元数据
        if (metadata.getTitleEn() != null && !metadata.getTitleEn().isEmpty()) {
            writer.write(String.format("# Title : \"%s\"", metadata.getTitleEn()));
            writer.newLine();
        }
        if (metadata.getDataProviderEn() != null && !metadata.getDataProviderEn().isEmpty()) {
            writer.write(String.format("# DataProvider : %s", metadata.getDataProviderEn()));
            writer.newLine();
        }
        if (metadata.getFieldEn() != null && !metadata.getFieldEn().isEmpty()) {
            writer.write(String.format("# Field : %s", metadata.getFieldEn()));
            writer.newLine();
        }
        if (metadata.getFieldNameEn() != null && !metadata.getFieldNameEn().isEmpty()) {
            writer.write(String.format("# FieldName : %s", metadata.getFieldNameEn()));
            writer.newLine();
        }
        if (metadata.getFieldTypeEn() != null && !metadata.getFieldTypeEn().isEmpty()) {
            writer.write(String.format("# FieldType : %s", metadata.getFieldTypeEn()));
            writer.newLine();
        }
        writer.write(String.format("# Count : %d", metadata.getCount()));
        writer.newLine();
        if (metadata.getRemarkEn() != null && !metadata.getRemarkEn().isEmpty()) {
            writer.write(String.format("# Remark : \"%s\"", metadata.getRemarkEn()));
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
        if (metadata.getTitle() != null && !metadata.getTitle().isEmpty()) {
            builder.append(String.format("# 标题 : \"%s\"\n", metadata.getTitle()));
        }
        if (metadata.getDataProvider() != null && !metadata.getDataProvider().isEmpty()) {
            builder.append(String.format("# 数据供应商 : %s\n", metadata.getDataProvider()));
        }
        if (metadata.getField() != null && !metadata.getField().isEmpty()) {
            builder.append(String.format("# 字段 : %s\n", metadata.getField()));
        }
        if (metadata.getFieldName() != null && !metadata.getFieldName().isEmpty()) {
            builder.append(String.format("# 字段名称 : %s\n", metadata.getFieldName()));
        }
        if (metadata.getFieldType() != null && !metadata.getFieldType().isEmpty()) {
            builder.append(String.format("# 字段类型 : %s\n", metadata.getFieldType()));
        }
        builder.append(String.format("# 计数 : %d\n", metadata.getCount()));
        if (metadata.getRemark() != null && !metadata.getRemark().isEmpty()) {
            builder.append(String.format("# 备注 : \"%s\"\n", metadata.getRemark()));
        }

        // 英文元数据
        if (metadata.getTitleEn() != null && !metadata.getTitleEn().isEmpty()) {
            builder.append(String.format("# Title : \"%s\"\n", metadata.getTitleEn()));
        }
        if (metadata.getDataProviderEn() != null && !metadata.getDataProviderEn().isEmpty()) {
            builder.append(String.format("# DataProvider : %s\n", metadata.getDataProviderEn()));
        }
        if (metadata.getFieldEn() != null && !metadata.getFieldEn().isEmpty()) {
            builder.append(String.format("# Field : %s\n", metadata.getFieldEn()));
        }
        if (metadata.getFieldNameEn() != null && !metadata.getFieldNameEn().isEmpty()) {
            builder.append(String.format("# FieldName : %s\n", metadata.getFieldNameEn()));
        }
        if (metadata.getFieldTypeEn() != null && !metadata.getFieldTypeEn().isEmpty()) {
            builder.append(String.format("# FieldType : %s\n", metadata.getFieldTypeEn()));
        }
        builder.append(String.format("# Count : %d\n", metadata.getCount()));
        if (metadata.getRemarkEn() != null && !metadata.getRemarkEn().isEmpty()) {
            builder.append(String.format("# Remark : \"%s\"\n", metadata.getRemarkEn()));
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
package com.acanx.meta.base.file.mvsv;

import java.util.ArrayList;
import java.util.List;

/**
 * MVSV 文件数据
 *
 * MVSV（Metadata Vertical bar Separated Values）格式文件的完整数据结构，
 * 包含元数据、字段名列表、字段名称列表、字段类型列表和数据行。
 *
 * @author ACANX
 * @since 2026-05-21
 */
public class Data {

    /** 元数据 */
    private Metadata metadata;

    /** 字段名列表 */
    private List<String> headers;

    /** 字段中文名称列表 */
    private List<String> fieldNames;

    /** 字段类型列表 */
    private List<String> fieldTypes;

    /** 数据行 */
    private List<List<String>> rows;

    /**
     * 默认构造函数
     */
    public Data() {
        this.headers = new ArrayList<>();
        this.fieldNames = new ArrayList<>();
        this.fieldTypes = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    /**
     * 获取元数据
     *
     * @return 元数据
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * 设置元数据
     *
     * @param metadata 元数据
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * 获取字段名列表
     *
     * @return 字段名列表
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * 设置字段名列表
     *
     * @param headers 字段名列表
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * 获取字段中文名称列表
     *
     * @return 字段中文名称列表
     */
    public List<String> getFieldNames() {
        return fieldNames;
    }

    /**
     * 设置字段中文名称列表
     *
     * @param fieldNames 字段中文名称列表
     */
    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    /**
     * 获取字段类型列表
     *
     * @return 字段类型列表
     */
    public List<String> getFieldTypes() {
        return fieldTypes;
    }

    /**
     * 设置字段类型列表
     *
     * @param fieldTypes 字段类型列表
     */
    public void setFieldTypes(List<String> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    /**
     * 获取数据行
     *
     * @return 数据行
     */
    public List<List<String>> getRows() {
        return rows;
    }

    /**
     * 设置数据行
     *
     * @param rows 数据行
     */
    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    /**
     * 添加一行数据
     *
     * @param row 数据行
     */
    public void addRow(List<String> row) {
        this.rows.add(row);
    }

    /**
     * 获取数据行数
     *
     * @return 数据行数
     */
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public String toString() {
        return "Data{" +
                "metadata=" + metadata +
                ", headers=" + headers +
                ", fieldNames=" + fieldNames +
                ", fieldTypes=" + fieldTypes +
                ", rowCount=" + rows.size() +
                '}';
    }
}
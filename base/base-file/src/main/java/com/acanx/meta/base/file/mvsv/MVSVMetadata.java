package com.acanx.meta.base.file.mvsv;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MVSV 文件元数据
 *
 * MVSV（Metadata Vertical bar Separated Values）格式文件的元数据结构，
 * 用于描述数据文件的格式、字段、类型等信息。
 *
 * @author ACANX
 * @since 2026-05-21
 */
public class MVSVMetadata {

    /** 标题 */
    private String title;

    /** Title（英文） */
    private String titleEn;

    /** 数据供应商 */
    private String dataProvider;

    /** DataProvider（英文） */
    private String dataProviderEn;

    /** 字段（英文字段名） */
    private String field;

    /** Field（英文） */
    private String fieldEn;

    /** 字段名称（中文字段名） */
    private String fieldName;

    /** FieldName（英文） */
    private String fieldNameEn;

    /** 字段类型 */
    private String fieldType;

    /** FieldType（英文） */
    private String fieldTypeEn;

    /** 计数（数据行数） */
    private int count;

    /** 备注 */
    private String remark;

    /** Remark（英文） */
    private String remarkEn;

    /** 扩展字段 */
    private Map<String, String> extra = new HashMap<>();

    /**
     * 默认构造函数
     */
    public MVSVMetadata() {
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取英文标题
     *
     * @return 英文标题
     */
    public String getTitleEn() {
        return titleEn;
    }

    /**
     * 设置英文标题
     *
     * @param titleEn 英文标题
     */
    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    /**
     * 获取数据供应商
     *
     * @return 数据供应商
     */
    public String getDataProvider() {
        return dataProvider;
    }

    /**
     * 设置数据供应商
     *
     * @param dataProvider 数据供应商
     */
    public void setDataProvider(String dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * 获取英文数据供应商
     *
     * @return 英文数据供应商
     */
    public String getDataProviderEn() {
        return dataProviderEn;
    }

    /**
     * 设置英文数据供应商
     *
     * @param dataProviderEn 英文数据供应商
     */
    public void setDataProviderEn(String dataProviderEn) {
        this.dataProviderEn = dataProviderEn;
    }

    /**
     * 获取字段定义
     *
     * @return 字段定义
     */
    public String getField() {
        return field;
    }

    /**
     * 设置字段定义
     *
     * @param field 字段定义
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * 获取英文字段定义
     *
     * @return 英文字段定义
     */
    public String getFieldEn() {
        return fieldEn;
    }

    /**
     * 设置英文字段定义
     *
     * @param fieldEn 英文字段定义
     */
    public void setFieldEn(String fieldEn) {
        this.fieldEn = fieldEn;
    }

    /**
     * 获取字段名称
     *
     * @return 字段名称
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置字段名称
     *
     * @param fieldName 字段名称
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 获取英文字段名称
     *
     * @return 英文字段名称
     */
    public String getFieldNameEn() {
        return fieldNameEn;
    }

    /**
     * 设置英文字段名称
     *
     * @param fieldNameEn 英文字段名称
     */
    public void setFieldNameEn(String fieldNameEn) {
        this.fieldNameEn = fieldNameEn;
    }

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * 设置字段类型
     *
     * @param fieldType 字段类型
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * 获取英文字段类型
     *
     * @return 英文字段类型
     */
    public String getFieldTypeEn() {
        return fieldTypeEn;
    }

    /**
     * 设置英文字段类型
     *
     * @param fieldTypeEn 英文字段类型
     */
    public void setFieldTypeEn(String fieldTypeEn) {
        this.fieldTypeEn = fieldTypeEn;
    }

    /**
     * 获取数据行数
     *
     * @return 数据行数
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置数据行数
     *
     * @param count 数据行数
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取英文备注
     *
     * @return 英文备注
     */
    public String getRemarkEn() {
        return remarkEn;
    }

    /**
     * 设置英文备注
     *
     * @param remarkEn 英文备注
     */
    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }

    /**
     * 获取扩展字段
     *
     * @return 扩展字段
     */
    public Map<String, String> getExtra() {
        return extra;
    }

    /**
     * 设置扩展字段
     *
     * @param extra 扩展字段
     */
    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    /**
     * 获取字段名列表
     *
     * @return 字段名列表
     */
    public List<String> getFieldList() {
        if (field == null || field.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(field.split("\\|"));
    }

    /**
     * 获取字段中文名称列表
     *
     * @return 字段中文名称列表
     */
    public List<String> getFieldNameList() {
        if (fieldName == null || fieldName.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(fieldName.split("\\|"));
    }

    /**
     * 获取字段类型列表
     *
     * @return 字段类型列表
     */
    public List<String> getFieldTypeList() {
        if (fieldType == null || fieldType.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(fieldType.split("\\|"));
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "title='" + title + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", dataProvider='" + dataProvider + '\'' +
                ", field='" + field + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", count=" + count +
                '}';
    }
}
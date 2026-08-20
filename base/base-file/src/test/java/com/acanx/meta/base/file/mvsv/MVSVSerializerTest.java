package com.acanx.meta.base.file.mvsv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MVSVSerializer 序列化器测试类
 *
 * 覆盖 serialize 与 serializeToString 的元数据区/数据区写入逻辑。
 *
 * @author BeeAgent
 * @since 2026-08-20
 */
class MVSVSerializerTest {

    /**
     * 构造包含中英文元数据与一行数据的 MVSVData
     */
    private MVSVData buildData() {
        MVSVMetadata metadata = new MVSVMetadata();
        metadata.setTitle("黄金分钟级行情");
        metadata.setTitleEn("Gold Minute-level Quotes");
        metadata.setDataProvider("xxx行情采集程序");
        metadata.setDataProviderEn("xxx Quote Collector");
        metadata.setField("Timestamp|Open|High");
        metadata.setFieldEn("Timestamp|Open|High");
        metadata.setFieldName("时间戳|开盘|最高");
        metadata.setFieldNameEn("时间戳|开盘|最高");
        metadata.setFieldType("timestamp|number|number");
        metadata.setFieldTypeEn("timestamp|number|number");
        metadata.setCount(1);
        metadata.setRemark("备注");
        metadata.setRemarkEn("Remark");

        MVSVData data = new MVSVData();
        data.setMetadata(metadata);
        data.setRows(Arrays.asList(
                Arrays.asList("2026-05-21 09:00:00", "100.0", "101.0")
        ));
        return data;
    }

    @Test
    void shouldSerializeToString() {
        MVSVSerializer serializer = new MVSVSerializer();
        String result = serializer.serializeToString(buildData());

        // 中文元数据区
        assertTrue(result.contains("# 标题 : \"黄金分钟级行情\""));
        assertTrue(result.contains("# 数据供应商 : xxx行情采集程序"));
        assertTrue(result.contains("# 字段 : Timestamp|Open|High"));
        assertTrue(result.contains("# 字段名称 : 时间戳|开盘|最高"));
        assertTrue(result.contains("# 字段类型 : timestamp|number|number"));
        assertTrue(result.contains("# 计数 : 1"));
        assertTrue(result.contains("# 备注 : \"备注\""));

        // 英文元数据区
        assertTrue(result.contains("# Title : \"Gold Minute-level Quotes\""));
        assertTrue(result.contains("# DataProvider : xxx Quote Collector"));
        assertTrue(result.contains("# Field : Timestamp|Open|High"));
        assertTrue(result.contains("# FieldName : 时间戳|开盘|最高"));
        assertTrue(result.contains("# FieldType : timestamp|number|number"));
        assertTrue(result.contains("# Count : 1"));
        assertTrue(result.contains("# Remark : \"Remark\""));

        // 数据区
        assertTrue(result.contains("2026-05-21 09:00:00|100.0|101.0"));
    }

    @Test
    void shouldSerializeToFile(@TempDir Path tempDir) throws IOException {
        MVSVSerializer serializer = new MVSVSerializer();
        Path file = tempDir.resolve("test.mvsv");
        serializer.serialize(buildData(), file.toString());

        String content = Files.readString(file);
        assertTrue(content.contains("# 标题 : \"黄金分钟级行情\""));
        assertTrue(content.contains("# 计数 : 1"));
        assertTrue(content.contains("2026-05-21 09:00:00|100.0|101.0"));
    }
}

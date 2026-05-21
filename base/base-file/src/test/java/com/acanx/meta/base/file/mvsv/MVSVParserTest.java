package com.acanx.meta.base.file.mvsv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MVSV 解析器测试类
 *
 * @author ACANX
 * @since 2026-05-21
 */
class MVSVParserTest {

    // 测试数据
    private static final String TEST_MVSV_CONTENT = """
# 标题 : "黄金分钟级行情 - 2026-05-21"
# Title : "Gold Minute-level Quotes - 2026-05-21"
# 数据供应商 : xxx行情采集程序
# DataProvider : xxx Quote Collector
# 字段 : Timestamp|Open|High|Low|Close|Volume
# Field : Timestamp|Open|High|Low|Close|Volume
# 字段名称 : 时间戳|开盘|最高|最低|收盘|成交量
# FieldName : 时间戳|开盘|最高|最低|收盘|成交量
# 字段类型 : timestamp|number|number|number|number|integer
# FieldType : timestamp|number|number|number|number|integer
# 计数 : 3
# Count : 3
# 时区 : Asia/Shanghai
# Timezone : Asia/Shanghai
# 货币 : CNY
# Currency : CNY
# 单位 : 元/克
# Unit : CNY/g
# 备注 : "开盘2345.00 收盘2350.00"
# Remark : "Open 2345.00 Close 2350.00"

09:00|2345.00|2350.00|2340.00|2348.50|12345
09:01|2348.50|2352.00|2347.00|2351.00|13456
09:02|2351.00|2355.00|2350.00|2353.50|14567
""";

    // 测试纯数据（无元数据）
    private static final String TEST_PURE_DATA_CONTENT = """
Timestamp|Open|High|Low|Close|Volume
09:00|2345.00|2350.00|2340.00|2348.50|12345
09:01|2348.50|2352.00|2347.00|2351.00|13456
""";

    // 测试空值处理
    private static final String TEST_NULL_VALUE_CONTENT = """
# 字段 : Timestamp|Open|High|Low|Close|Volume
# Field : Timestamp|Open|High|Low|Close|Volume

09:00|2345.00|2350.00||2348.50|12345
09:01|2348.50||2347.00|2351.00|13456
""";

    @Test
    void testParse(@TempDir Path tempDir) throws IOException {
        // 创建临时测试文件
        Path testFile = tempDir.resolve("test.mvsv");
        Files.writeString(testFile, TEST_MVSV_CONTENT);

        // 解析文件
        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parse(testFile.toString());

        // 验证元数据
        assertEquals("黄金分钟级行情 - 2026-05-21", data.getMetadata().getTitle());
        assertEquals("Gold Minute-level Quotes - 2026-05-21", data.getMetadata().getTitleEn());
        assertEquals("xxx行情采集程序", data.getMetadata().getDataProvider());
        assertEquals("Timestamp|Open|High|Low|Close|Volume", data.getMetadata().getField());
        assertEquals("时间戳|开盘|最高|最低|收盘|成交量", data.getMetadata().getFieldName());
        assertEquals("timestamp|number|number|number|number|integer", data.getMetadata().getFieldType());
        assertEquals(3, data.getMetadata().getCount());

        // 验证字段列表
        List<String> fieldList = data.getMetadata().getFieldList();
        assertEquals(6, fieldList.size());
        assertEquals("Timestamp", fieldList.get(0));
        assertEquals("Open", fieldList.get(1));
        assertEquals("High", fieldList.get(2));
        assertEquals("Low", fieldList.get(3));
        assertEquals("Close", fieldList.get(4));
        assertEquals("Volume", fieldList.get(5));

        // 验证字段名称列表
        List<String> fieldNameList = data.getMetadata().getFieldNameList();
        assertEquals(6, fieldNameList.size());
        assertEquals("时间戳", fieldNameList.get(0));
        assertEquals("开盘", fieldNameList.get(1));
        assertEquals("最高", fieldNameList.get(2));
        assertEquals("最低", fieldNameList.get(3));
        assertEquals("收盘", fieldNameList.get(4));
        assertEquals("成交量", fieldNameList.get(5));

        // 验证字段类型列表
        List<String> fieldTypeList = data.getMetadata().getFieldTypeList();
        assertEquals(6, fieldTypeList.size());
        assertEquals("timestamp", fieldTypeList.get(0));
        assertEquals("number", fieldTypeList.get(1));
        assertEquals("number", fieldTypeList.get(2));
        assertEquals("number", fieldTypeList.get(3));
        assertEquals("number", fieldTypeList.get(4));
        assertEquals("integer", fieldTypeList.get(5));

        // 验证数据行
        assertEquals(3, data.getRows().size());

        // 验证第一行数据
        List<String> row1 = data.getRows().get(0);
        assertEquals("09:00", row1.get(0));
        assertEquals("2345.00", row1.get(1));
        assertEquals("2350.00", row1.get(2));
        assertEquals("2340.00", row1.get(3));
        assertEquals("2348.50", row1.get(4));
        assertEquals("12345", row1.get(5));
    }

    @Test
    void testParseString() {
        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parseString(TEST_MVSV_CONTENT);

        // 验证基本解析
        assertEquals("黄金分钟级行情 - 2026-05-21", data.getMetadata().getTitle());
        assertEquals(3, data.getRows().size());
    }

    @Test
    void testParsePureMVSVData(@TempDir Path tempDir) throws IOException {
        // 测试纯数据（无元数据）
        Path testFile = tempDir.resolve("pure.mvsv");
        Files.writeString(testFile, TEST_PURE_DATA_CONTENT);

        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parse(testFile.toString());

        // 验证无元数据时解析正常
        assertNull(data.getMetadata().getTitle());

        // 无元数据时，headers 为 null
        assertNull(data.getHeaders());

        // 所有行都是数据
        assertEquals(2, data.getRows().size());
    }

    @Test
    void testParseNullValue(@TempDir Path tempDir) throws IOException {
        // 测试空值处理
        Path testFile = tempDir.resolve("null.mvsv");
        Files.writeString(testFile, TEST_NULL_VALUE_CONTENT);

        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parse(testFile.toString());

        // 验证空值处理
        assertEquals(2, data.getRows().size());

        // 第一行 Low 字段为空
        assertEquals("", data.getRows().get(0).get(3));

        // 第二行 High 字段为空
        assertEquals("", data.getRows().get(1).get(2));
    }

    @Test
    void testMetadataGetMethods() {
        MVSVMetadata metadata = new MVSVMetadata();
        metadata.setField("A|B|C|D");
        metadata.setFieldName("名称A|名称B|名称C|名称D");
        metadata.setFieldType("string|number|integer|boolean");

        // 测试 getFieldList
        List<String> fieldList = metadata.getFieldList();
        assertEquals(4, fieldList.size());

        // 测试 getFieldNameList
        List<String> fieldNameList = metadata.getFieldNameList();
        assertEquals(4, fieldNameList.size());

        // 测试 getFieldTypeList
        List<String> fieldTypeList = metadata.getFieldTypeList();
        assertEquals(4, fieldTypeList.size());

        // 测试空值情况
        MVSVMetadata emptyMVSVMetadata = new MVSVMetadata();
        assertTrue(emptyMVSVMetadata.getFieldList().isEmpty());
        assertTrue(emptyMVSVMetadata.getFieldNameList().isEmpty());
        assertTrue(emptyMVSVMetadata.getFieldTypeList().isEmpty());
    }

    @Test
    void testChineseEnglishMetadata() {
        // 测试中英双语元数据解析
        String content = """
# 标题 : "中文标题"
# Title : "English Title"
# 字段 : A|B|C
# Field : A|B|C
# 计数 : 5
# Count : 5

1|2|3
""";

        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parseString(content);

        // 验证中英双语字段都正确解析
        assertEquals("中文标题", data.getMetadata().getTitle());
        assertEquals("English Title", data.getMetadata().getTitleEn());

        // 验证 Count 可以从中文或英文字段获取
        assertEquals(5, data.getMetadata().getCount());
    }

    @Test
    void testQuotedValue() {
        // 测试带引号的值
        String content = """
# 标题 : "带引号的标题"
# 备注 : "带空格的备注信息"

A|B|C
1|2|3
""";

        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parseString(content);

        assertEquals("带引号的标题", data.getMetadata().getTitle());
        assertEquals("带空格的备注信息", data.getMetadata().getRemark());
    }

    @Test
    void testExtraFields() {
        // 测试扩展字段
        String content = """
# 标题 : "测试"
# 自定义字段 : 自定义值
# CustomField : CustomValue
# 数据质量 : A
# DataQuality : A

A|B
1|2
""";

        MVSVParser parser = new MVSVParser();
        MVSVData data = parser.parseString(content);

        // 验证扩展字段存储在 Extra 中
        assertEquals("自定义值", data.getMetadata().getExtra().get("自定义字段"));
        assertEquals("CustomValue", data.getMetadata().getExtra().get("CustomField"));
        assertEquals("A", data.getMetadata().getExtra().get("数据质量"));
    }
}
package com.acanx.meta.base.exception;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BusinessException 测试类
 *
 * @author ACANX
 * @since 0.8.7
 */
class BusinessExceptionTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        BusinessException ex = new BusinessException();
        assertNull(ex.getCode());
        assertNull(ex.getMessage());
        assertNull(ex.getModule());
        assertNull(ex.getOperation());
        assertNull(ex.getDetails());
    }

    @Test
    void shouldCreateWithMessage() {
        BusinessException ex = new BusinessException("business error");
        assertEquals("business error", ex.getMessage());
        assertNull(ex.getModule());
        assertNull(ex.getOperation());
    }

    @Test
    void shouldCreateWithCodeAndMessage() {
        BusinessException ex = new BusinessException("BIZ_001", "order failed");
        assertEquals("BIZ_001", ex.getCode());
        assertEquals("order failed", ex.getMessage());
    }

    @Test
    void shouldCreateWithCodeMessageAndCause() {
        Throwable cause = new RuntimeException("validation failed");
        BusinessException ex = new BusinessException("BIZ_002", "invalid data", cause);
        assertEquals("BIZ_002", ex.getCode());
        assertEquals("invalid data", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void shouldCreateWithCodeMessageAndArgs() {
        BusinessException ex = new BusinessException("BIZ_003", "item {0} out of stock", "SKU-001");
        assertEquals("BIZ_003", ex.getCode());
        assertArrayEquals(new Object[]{"SKU-001"}, ex.getArgs());
    }

    @Test
    void shouldCreateWithCodeMessageCauseAndArgs() {
        Throwable cause = new RuntimeException("db timeout");
        BusinessException ex = new BusinessException("BIZ_004", "save failed for {0}", cause, "order-456");
        assertEquals("BIZ_004", ex.getCode());
        assertSame(cause, ex.getCause());
        assertArrayEquals(new Object[]{"order-456"}, ex.getArgs());
    }

    @Test
    void toStringShouldIncludeModuleAndOperation() {
        BusinessException ex = new BusinessException("BIZ_005", "payment failed");
        String str = ex.toString();
        assertTrue(str.contains("BIZ_005"));
        assertTrue(str.contains("payment failed"));
    }
}

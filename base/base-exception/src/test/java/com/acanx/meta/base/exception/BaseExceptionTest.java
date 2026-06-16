package com.acanx.meta.base.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BaseException 测试类
 *
 * @author ACANX
 * @since 0.8.7
 */
class BaseExceptionTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        BaseException ex = new BaseException();
        assertNull(ex.getCode());
        assertNull(ex.getMessage());
        assertNull(ex.getArgs());
    }

    @Test
    void shouldCreateWithMessage() {
        BaseException ex = new BaseException("test message");
        assertNull(ex.getCode());
        assertEquals("test message", ex.getMessage());
        assertNull(ex.getArgs());
    }

    @Test
    void shouldCreateWithCodeAndMessage() {
        BaseException ex = new BaseException("ERR_001", "error occurred");
        assertEquals("ERR_001", ex.getCode());
        assertEquals("error occurred", ex.getMessage());
        assertNull(ex.getArgs());
    }

    @Test
    void shouldCreateWithCodeMessageAndCause() {
        Throwable cause = new RuntimeException("root cause");
        BaseException ex = new BaseException("ERR_002", "wrapped error", cause);
        assertEquals("ERR_002", ex.getCode());
        assertEquals("wrapped error", ex.getMessage());
        assertNull(ex.getArgs());
        assertSame(cause, ex.getCause());
    }

    @Test
    void shouldCreateWithCodeMessageAndArgs() {
        BaseException ex = new BaseException("ERR_003", "user {0} not found", "张三");
        assertEquals("ERR_003", ex.getCode());
        assertEquals("user {0} not found", ex.getMessage());
        assertArrayEquals(new Object[]{"张三"}, ex.getArgs());
    }

    @Test
    void shouldCreateWithCodeMessageCauseAndArgs() {
        Throwable cause = new RuntimeException("db error");
        BaseException ex = new BaseException("ERR_004", "operation failed for {0}", cause, "order-123");
        assertEquals("ERR_004", ex.getCode());
        assertEquals("operation failed for {0}", ex.getMessage());
        assertSame(cause, ex.getCause());
        assertArrayEquals(new Object[]{"order-123"}, ex.getArgs());
    }

    @Test
    void toStringShouldIncludeAllFields() {
        BaseException ex = new BaseException("ERR_005", "something went wrong", "detail1");
        String str = ex.toString();
        assertTrue(str.contains("ERR_005"));
        assertTrue(str.contains("something went wrong"));
        assertTrue(str.contains("detail1"));
    }

    @Test
    void toStringShouldHandleNullFields() {
        BaseException ex = new BaseException();
        String str = ex.toString();
        assertTrue(str.contains("BaseException"));
        assertTrue(str.contains("{"));
        assertTrue(str.contains("}"));
    }
}

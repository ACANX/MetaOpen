package com.acanx.meta.base.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SystemException 测试类
 *
 * @author ACANX
 * @since 0.8.7
 */
class SystemExceptionTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        SystemException ex = new SystemException();
        assertNull(ex.getCode());
        assertNull(ex.getMessage());
        assertTrue(ex.isAlert());
        assertFalse(ex.isRecoverable());
    }

    @Test
    void shouldCreateWithMessage() {
        SystemException ex = new SystemException("system error");
        assertEquals("system error", ex.getMessage());
        assertTrue(ex.isAlert());
        assertFalse(ex.isRecoverable());
    }

    @Test
    void shouldCreateWithCodeAndMessage() {
        SystemException ex = new SystemException("SYS_001", "db connection pool exhausted");
        assertEquals("SYS_001", ex.getCode());
        assertEquals("db connection pool exhausted", ex.getMessage());
        assertTrue(ex.isAlert());
        assertFalse(ex.isRecoverable());
    }

    @Test
    void shouldCreateWithCodeMessageAndCause() {
        Throwable cause = new RuntimeException("connection refused");
        SystemException ex = new SystemException("SYS_002", "service unavailable", cause);
        assertEquals("SYS_002", ex.getCode());
        assertEquals("service unavailable", ex.getMessage());
        assertSame(cause, ex.getCause());
        assertTrue(ex.isAlert());
    }

    @Test
    void shouldCreateWithCodeMessageAndArgs() {
        SystemException ex = new SystemException("SYS_003", "cache {0} unavailable", "redis-cluster");
        assertEquals("SYS_003", ex.getCode());
        assertArrayEquals(new Object[]{"redis-cluster"}, ex.getArgs());
        assertTrue(ex.isAlert());
    }

    @Test
    void shouldCreateWithCodeMessageCauseAndArgs() {
        Throwable cause = new RuntimeException("OOM");
        SystemException ex = new SystemException("SYS_004", "jvm resource exhausted for {0}", cause, "thread-pool");
        assertEquals("SYS_004", ex.getCode());
        assertSame(cause, ex.getCause());
        assertArrayEquals(new Object[]{"thread-pool"}, ex.getArgs());
        assertTrue(ex.isAlert());
    }

    @Test
    void toStringShouldIncludeAlertAndRecoverable() {
        SystemException ex = new SystemException("SYS_005", "critical error");
        String str = ex.toString();
        assertTrue(str.contains("SYS_005"));
        assertTrue(str.contains("critical error"));
        assertTrue(str.contains("alert=true"));
        assertTrue(str.contains("recoverable=false"));
    }
}

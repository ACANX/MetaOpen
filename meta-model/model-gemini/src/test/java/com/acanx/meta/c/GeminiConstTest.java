package com.acanx.meta.c;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GeminiConst 测试类
 *
 * @author ACANX
 * @since 0.8.7
 */
class GeminiConstTest {

    @Test
    void shouldHaveDefaultModel() {
        assertEquals("gemini-3-pro-high", GeminiConst.MODEL);
    }

    @Test
    void shouldHaveGemini25FlashModel() {
        assertEquals("gemini-2.5-flash", GeminiConst.MODEL_GEMINI_2_5_FRESH);
    }

    @Test
    void shouldHaveGemini25ProModel() {
        assertEquals("gemini-2.5-pro", GeminiConst.MODEL_GEMINI_2_5_PRO);
    }

    @Test
    void shouldHaveGemini3Models() {
        assertEquals("gemini-3-flash-preview", GeminiConst.GEMINI_3_FLASH);
        assertEquals("gemini-3-pro-preview", GeminiConst.GEMINI_3_PRO);
        assertEquals("gemini-3-pro-low", GeminiConst.GEMINI_3_PRO_LOW);
        assertEquals("gemini-3-pro-high", GeminiConst.GEMINI_3_PRO_HIGH);
    }

    @Test
    void shouldHaveApiHost() {
        assertEquals("generativelanguage.googleapis.com", GeminiConst.API_HOST);
    }

    @Test
    void shouldHaveBaseDomain() {
        assertEquals("https://generativelanguage.googleapis.com", GeminiConst.BASE_DOMAIN);
    }

    @Test
    void shouldHaveApiVersions() {
        assertEquals("v1", GeminiConst.V1);
        assertEquals("v1beta", GeminiConst.V1_BETA);
    }

    @Test
    void shouldHaveUserAgent() {
        assertNotNull(GeminiConst.USER_AGENT);
        assertTrue(GeminiConst.USER_AGENT.contains("Mozilla"));
    }

    @Test
    void shouldHaveGenerateContentEndpoint() {
        assertTrue(GeminiConst.TPL_API_GENERATE_CONTENT_BETA.contains("v1beta"));
        assertTrue(GeminiConst.TPL_API_GENERATE_CONTENT_BETA.contains("generateContent"));
    }

    @Test
    void privateConstructorShouldNotBeAccessible() {
        assertThrows(IllegalAccessException.class, () -> {
            GeminiConst.class.getDeclaredConstructor().newInstance();
        });
    }
}

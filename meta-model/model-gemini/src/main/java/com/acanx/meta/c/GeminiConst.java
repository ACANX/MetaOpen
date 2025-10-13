package com.acanx.meta.c;

/**
 * GeminiConst
 *
 * @author ACANX
 * @since 20251008
 */
public class GeminiConst {


    // Gemini 2.5 Pro 模型名称
    public static final String MODEL_GEMINI_2_5_FRESH = "gemini-2.5-flash";
    public static final String MODEL_GEMINI_2_5_PRO = "gemini-2.5-pro";
    // Gemini API 配置
    public static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com";
    // Google AI API 的通用生成内容端点
    public static final String TPL_API_ENDPOINT = GEMINI_BASE_URL + "/v1/models/%s:generateContent?key=%s";
}

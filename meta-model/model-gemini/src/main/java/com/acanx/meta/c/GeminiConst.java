package com.acanx.meta.c;

/**
 * GeminiConst
 *
 * @author ACANX
 * @since 20251008
 */
public class GeminiConst {

    // 默认模型
    public static String MODEL = "gemini-3-pro-high";
    // Gemini 2.5 Pro 模型名称
    public static final String MODEL_GEMINI_2_5_FRESH = "gemini-2.5-flash";
    public static final String MODEL_GEMINI_2_5_PRO = "gemini-2.5-pro";
    // Gemini 3 Pro 模型名称
    public static final String GEMINI_3_FLASH = "gemini-3-flash-preview";
    public static final String GEMINI_3_PRO = "gemini-3-pro-preview";
    public static final String GEMINI_3_PRO_LOW = "gemini-3-pro-low";
    public static final String GEMINI_3_PRO_HIGH = "gemini-3-pro-high";

    public static final String API_HOST = "generativelanguage.googleapis.com";
    // Gemini API 配置
    public static final String BASE_DOMAIN = "https://" + API_HOST;

    public static final String V1 = "v1";
    public static final String V1_BETA = "v1beta";
    // Google AI API 的通用生成内容端点
    @Deprecated
    public static final String TPL_API_ENDPOINT = BASE_DOMAIN + "/v1/models/%s:generateContent?key=%s";

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36";


    public static final String TPL_API_GENERATE_CONTENT_BETA = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";


}

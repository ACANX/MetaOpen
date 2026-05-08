package com.acanx.meta.model.llm;

public final class LLMConst {

    public static final String HEADER_OPENAI_ORGANIZATION = "OpenAI-Organization";
    public static final String HEADER_X_GOOG_API_KEY = "x-goog-api-key";
    public static final String HEADER_X_API_KEY = "x-api-key";
    public static final String HEADER_ANTHROPIC_VERSION = "anthropic-version";

    public static final String PATH_CHAT_COMPLETIONS = "/chat/completions";
    public static final String PATH_MESSAGES = "/messages";
    public static final String PATH_MODELS = "/models";
    public static final String PATH_GENERATE_CONTENT = ":generateContent";
    public static final String PATH_V1 = "/v1";
    public static final String PATH_V1_BETA = "/v1beta";

    public static final String DEFAULT_GEMINI_API_VERSION = "v1beta";
    public static final String DEFAULT_ANTHROPIC_VERSION = "2023-06-01";

    private LLMConst() {
    }
}

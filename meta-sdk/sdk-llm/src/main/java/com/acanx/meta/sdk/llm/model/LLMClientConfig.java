package com.acanx.meta.sdk.llm.model;

import com.acanx.meta.sdk.llm.LLMClient;
import com.acanx.meta.sdk.llm.enums.ModelProvider;
import com.acanx.meta.model.llm.LLMConst;
import com.acanx.meta.model.llm.provider.LLMBaseUrls;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable configuration for {@link LLMClient}.
 */
public class LLMClientConfig {

    private final ModelProvider provider;
    private final String baseUrl;
    private final String apiKey;
    private final String organizationId;
    private final String geminiApiVersion;
    private final String anthropicVersion;
    private final Duration timeout;
    private final Map<String, String> headers;

    private LLMClientConfig(Builder builder) {
        this.provider = Objects.requireNonNull(builder.provider, "provider");
        this.baseUrl = trimTrailingSlash(resolveBaseUrl(builder.provider, builder.baseUrl));
        this.apiKey = builder.apiKey;
        this.organizationId = builder.organizationId;
        this.geminiApiVersion = builder.geminiApiVersion;
        this.anthropicVersion = builder.anthropicVersion;
        this.timeout = builder.timeout;
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(builder.headers));
    }

    public static Builder builder(ModelProvider provider) {
        return new Builder(provider);
    }

    public ModelProvider getProvider() {
        return provider;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getGeminiApiVersion() {
        return geminiApiVersion;
    }

    public String getAnthropicVersion() {
        return anthropicVersion;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private static String resolveBaseUrl(ModelProvider provider, String baseUrl) {
        if (baseUrl != null && !baseUrl.isBlank()) {
            return baseUrl;
        }
        return switch (provider) {
            case OPENAI, OPENAI_COMPATIBLE -> LLMBaseUrls.OPENAI;
            case DEEPSEEK -> LLMBaseUrls.DEEPSEEK_OPENAI;
            case GEMINI -> LLMBaseUrls.GEMINI;
            case ANTHROPIC, ANTHROPIC_COMPATIBLE -> LLMBaseUrls.ANTHROPIC;
        };
    }

    private static String trimTrailingSlash(String value) {
        String result = value;
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static class Builder {
        private final ModelProvider provider;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String geminiApiVersion = LLMConst.DEFAULT_GEMINI_API_VERSION;
        private String anthropicVersion = LLMConst.DEFAULT_ANTHROPIC_VERSION;
        private Duration timeout = Duration.ofSeconds(60);
        private final Map<String, String> headers = new LinkedHashMap<>();

        private Builder(ModelProvider provider) {
            this.provider = provider;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder geminiApiVersion(String geminiApiVersion) {
            this.geminiApiVersion = geminiApiVersion;
            return this;
        }

        public Builder anthropicVersion(String anthropicVersion) {
            this.anthropicVersion = anthropicVersion;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public LLMClientConfig build() {
            return new LLMClientConfig(this);
        }
    }
}

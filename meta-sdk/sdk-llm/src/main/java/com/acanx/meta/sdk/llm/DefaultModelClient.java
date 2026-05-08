package com.acanx.meta.sdk.llm;

import com.acanx.meta.base.http.c.BaseHttpConst;
import com.acanx.meta.model.llm.LLMConst;
import com.acanx.meta.sdk.llm.enums.ModelProvider;
import com.acanx.meta.sdk.llm.exception.ModelClientException;
import com.acanx.meta.sdk.llm.internal.AnthropicProtocol;
import com.acanx.meta.sdk.llm.internal.GeminiProtocol;
import com.acanx.meta.sdk.llm.internal.OpenAiCompatibleProtocol;
import com.acanx.meta.sdk.llm.internal.ProviderProtocol;
import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import com.acanx.meta.sdk.llm.model.LLMClientConfig;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

class DefaultModelClient implements LLMClient {

    private final LLMClientConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ProviderProtocol protocol;

    DefaultModelClient(LLMClientConfig config) {
        this(config, HttpClient.newBuilder().connectTimeout(config.getTimeout()).build());
    }

    DefaultModelClient(LLMClientConfig config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.protocol = createProtocol(config.getProvider());
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            String body = objectMapper.writeValueAsString(protocol.toRequestBody(request));
            HttpRequest.Builder httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(resolveRequestUrl(request)))
                    .timeout(config.getTimeout())
                    .header(BaseHttpConst.HEADER_CONTENT_TYPE, BaseHttpConst.MEDIA_TYPE_APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(body));
            applyHeaders(httpRequest);
            HttpResponse<String> response = httpClient.send(httpRequest.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < BaseHttpConst.STATUS_OK
                    || response.statusCode() >= BaseHttpConst.STATUS_MULTIPLE_CHOICES) {
                throw new ModelClientException("LLM API request failed", response.statusCode(), response.body());
            }
            return protocol.toChatResponse(response.body(), objectMapper);
        } catch (IOException e) {
            throw new ModelClientException("LLM API serialization or transport error", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ModelClientException("LLM API request interrupted", e);
        }
    }

    private void applyHeaders(HttpRequest.Builder builder) {
        if (config.getApiKey() != null && !config.getApiKey().isBlank()) {
            switch (config.getProvider()) {
                case GEMINI -> builder.header(LLMConst.HEADER_X_GOOG_API_KEY, config.getApiKey());
                case ANTHROPIC, ANTHROPIC_COMPATIBLE -> builder.header(LLMConst.HEADER_X_API_KEY, config.getApiKey());
                default -> builder.header(
                        BaseHttpConst.HEADER_AUTHORIZATION,
                        BaseHttpConst.AUTH_SCHEME_BEARER + " " + config.getApiKey());
            }
        }
        if (config.getOrganizationId() != null && !config.getOrganizationId().isBlank()) {
            builder.header(LLMConst.HEADER_OPENAI_ORGANIZATION, config.getOrganizationId());
        }
        if (config.getProvider() == ModelProvider.ANTHROPIC || config.getProvider() == ModelProvider.ANTHROPIC_COMPATIBLE) {
            builder.header(LLMConst.HEADER_ANTHROPIC_VERSION, config.getAnthropicVersion());
        }
        for (Map.Entry<String, String> header : config.getHeaders().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }
    }

    private static ProviderProtocol createProtocol(ModelProvider provider) {
        return switch (provider) {
            case GEMINI -> new GeminiProtocol();
            case ANTHROPIC, ANTHROPIC_COMPATIBLE -> new AnthropicProtocol();
            case OPENAI, OPENAI_COMPATIBLE, DEEPSEEK -> new OpenAiCompatibleProtocol();
        };
    }

    private String resolveRequestUrl(ChatRequest request) {
        if (config.getProvider() == ModelProvider.GEMINI) {
            if (hasGeminiApiVersion(config.getBaseUrl())) {
                return config.getBaseUrl() + protocol.path(request);
            }
            return config.getBaseUrl() + "/" + config.getGeminiApiVersion() + protocol.path(request);
        }
        return config.getBaseUrl() + protocol.path(request);
    }

    private static boolean hasGeminiApiVersion(String baseUrl) {
        return baseUrl.matches(".*/v\\d+(beta)?$");
    }
}

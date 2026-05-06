package com.acanx.meta.sdk.llm.internal;

import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
public interface ProviderProtocol {

    String path(ChatRequest request);

    Object toRequestBody(ChatRequest request);

    ChatResponse toChatResponse(String responseBody, ObjectMapper objectMapper) throws IOException;
}

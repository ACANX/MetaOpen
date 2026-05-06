package com.acanx.meta.sdk.llm.internal;

import com.acanx.meta.sdk.llm.model.ChatChoice;
import com.acanx.meta.sdk.llm.model.ChatMessage;
import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import com.acanx.meta.sdk.llm.model.ChatUsage;
import com.acanx.meta.model.llm.LLMConst;
import com.acanx.meta.sdk.llm.internal.request.AnthropicMessagesRequestBody;
import com.acanx.meta.sdk.llm.internal.response.AnthropicMessagesResponseBody;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnthropicProtocol implements ProviderProtocol {

    @Override
    public String path(ChatRequest request) {
        return LLMConst.PATH_MESSAGES;
    }

    @Override
    public AnthropicMessagesRequestBody toRequestBody(ChatRequest request) {
        return new AnthropicMessagesRequestBody(
                request.getModel(),
                toMessages(request.getMessages()),
                request.getTemperature(),
                request.getMaxTokens() == null ? 1024 : request.getMaxTokens(),
                systemPrompt(request.getMessages()));
    }

    @Override
    public ChatResponse toChatResponse(String responseBody, ObjectMapper objectMapper) throws IOException {
        AnthropicMessagesResponseBody body = objectMapper.readValue(responseBody, AnthropicMessagesResponseBody.class);
        ChatResponse response = new ChatResponse();
        response.setId(body.getId());
        response.setModel(body.getModel());
        response.setRawBody(responseBody);
        if (body.getUsage() != null) {
            Integer inputTokens = body.getUsage().getInputTokens();
            Integer outputTokens = body.getUsage().getOutputTokens();
            Integer totalTokens = inputTokens == null || outputTokens == null ? null : inputTokens + outputTokens;
            response.setUsage(new ChatUsage(inputTokens, outputTokens, totalTokens));
        }
        response.setChoices(List.of(new ChatChoice(
                0,
                ChatMessage.assistant(firstAnthropicText(body.getContent())),
                body.getStopReason())));
        return response;
    }

    private static List<AnthropicMessagesRequestBody.Message> toMessages(List<ChatMessage> messages) {
        List<AnthropicMessagesRequestBody.Message> result = new ArrayList<>();
        for (ChatMessage message : messages) {
            if ("system".equals(message.getRole())) {
                continue;
            }
            result.add(new AnthropicMessagesRequestBody.Message(
                    "assistant".equals(message.getRole()) ? "assistant" : "user",
                    message.getContent()));
        }
        return result;
    }

    private static String systemPrompt(List<ChatMessage> messages) {
        StringBuilder builder = new StringBuilder();
        for (ChatMessage message : messages) {
            if ("system".equals(message.getRole()) && message.getContent() != null) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }
                builder.append(message.getContent());
            }
        }
        return builder.isEmpty() ? null : builder.toString();
    }

    private static String firstAnthropicText(List<AnthropicMessagesResponseBody.Content> content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        return content.get(0).getText();
    }
}

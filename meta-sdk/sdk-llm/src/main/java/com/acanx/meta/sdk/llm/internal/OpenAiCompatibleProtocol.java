package com.acanx.meta.sdk.llm.internal;

import com.acanx.meta.sdk.llm.model.ChatChoice;
import com.acanx.meta.sdk.llm.model.ChatMessage;
import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import com.acanx.meta.sdk.llm.model.ChatUsage;
import com.acanx.meta.model.llm.LLMConst;
import com.acanx.meta.sdk.llm.internal.request.OpenAiChatRequestBody;
import com.acanx.meta.sdk.llm.internal.response.OpenAiChatResponseBody;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenAiCompatibleProtocol implements ProviderProtocol {

    @Override
    public String path(ChatRequest request) {
        return LLMConst.PATH_CHAT_COMPLETIONS;
    }

    @Override
    public OpenAiChatRequestBody toRequestBody(ChatRequest request) {
        return new OpenAiChatRequestBody(
                request.getModel(),
                request.getMessages(),
                request.getStream(),
                request.getTemperature(),
                request.getMaxTokens());
    }

    @Override
    public ChatResponse toChatResponse(String responseBody, ObjectMapper objectMapper) throws IOException {
        OpenAiChatResponseBody body = objectMapper.readValue(responseBody, OpenAiChatResponseBody.class);
        ChatResponse response = new ChatResponse();
        response.setId(body.getId());
        response.setModel(body.getModel());
        response.setRawBody(responseBody);
        if (body.getUsage() != null) {
            response.setUsage(new ChatUsage(
                    body.getUsage().getPromptTokens(),
                    body.getUsage().getCompletionTokens(),
                    body.getUsage().getTotalTokens()));
        }
        List<ChatChoice> choices = new ArrayList<>();
        List<OpenAiChatResponseBody.Choice> responseChoices = body.getChoices() == null ? List.of() : body.getChoices();
        for (int i = 0; i < responseChoices.size(); i++) {
            OpenAiChatResponseBody.Choice choice = responseChoices.get(i);
            ChatMessage message = choice.getMessage() == null ? new ChatMessage() : choice.getMessage();
            choices.add(new ChatChoice(
                    choice.getIndex() == null ? i : choice.getIndex(),
                    message,
                    choice.getFinishReason()));
        }
        response.setChoices(choices);
        return response;
    }
}

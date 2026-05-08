package com.acanx.meta.sdk.llm.internal;

import com.acanx.meta.sdk.llm.model.ChatChoice;
import com.acanx.meta.sdk.llm.model.ChatMessage;
import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import com.acanx.meta.sdk.llm.model.ChatUsage;
import com.acanx.meta.model.llm.LLMConst;
import com.acanx.meta.sdk.llm.internal.request.GeminiGenerateContentRequestBody;
import com.acanx.meta.sdk.llm.internal.response.GeminiGenerateContentResponseBody;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeminiProtocol implements ProviderProtocol {

    @Override
    public String path(ChatRequest request) {
        return LLMConst.PATH_MODELS + "/" + request.getModel() + LLMConst.PATH_GENERATE_CONTENT;
    }

    @Override
    public GeminiGenerateContentRequestBody toRequestBody(ChatRequest request) {
        GeminiGenerateContentRequestBody.GenerationConfig generationConfig = null;
        if (request.getTemperature() != null || request.getMaxTokens() != null) {
            generationConfig = new GeminiGenerateContentRequestBody.GenerationConfig(
                    request.getTemperature(),
                    request.getMaxTokens());
        }
        return new GeminiGenerateContentRequestBody(toContents(request.getMessages()), generationConfig);
    }

    @Override
    public ChatResponse toChatResponse(String responseBody, ObjectMapper objectMapper) throws IOException {
        GeminiGenerateContentResponseBody body = objectMapper.readValue(responseBody, GeminiGenerateContentResponseBody.class);
        ChatResponse response = new ChatResponse();
        response.setModel(body.getModelVersion());
        response.setRawBody(responseBody);
        if (body.getUsageMetadata() != null) {
            response.setUsage(new ChatUsage(
                    body.getUsageMetadata().getPromptTokenCount(),
                    body.getUsageMetadata().getCandidatesTokenCount(),
                    body.getUsageMetadata().getTotalTokenCount()));
        }
        List<ChatChoice> choices = new ArrayList<>();
        List<GeminiGenerateContentResponseBody.Candidate> candidates =
                body.getCandidates() == null ? List.of() : body.getCandidates();
        for (int i = 0; i < candidates.size(); i++) {
            GeminiGenerateContentResponseBody.Candidate candidate = candidates.get(i);
            choices.add(new ChatChoice(
                    i,
                    ChatMessage.assistant(firstGeminiText(candidate.getContent())),
                    candidate.getFinishReason()));
        }
        response.setChoices(choices);
        return response;
    }

    private static List<GeminiGenerateContentRequestBody.Content> toContents(List<ChatMessage> messages) {
        List<GeminiGenerateContentRequestBody.Content> contents = new ArrayList<>();
        for (ChatMessage message : messages) {
            if ("system".equals(message.getRole())) {
                continue;
            }
            contents.add(new GeminiGenerateContentRequestBody.Content(
                    "assistant".equals(message.getRole()) ? "model" : "user",
                    List.of(new GeminiGenerateContentRequestBody.Part(message.getContent()))));
        }
        return contents;
    }

    private static String firstGeminiText(GeminiGenerateContentResponseBody.Content content) {
        if (content == null || content.getParts() == null || content.getParts().isEmpty()) {
            return null;
        }
        return content.getParts().get(0).getText();
    }
}

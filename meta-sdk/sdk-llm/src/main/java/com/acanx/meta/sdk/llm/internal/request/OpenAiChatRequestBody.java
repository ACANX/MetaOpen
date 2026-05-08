package com.acanx.meta.sdk.llm.internal.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.acanx.meta.sdk.llm.model.ChatMessage;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenAiChatRequestBody {

    private String model;
    private List<ChatMessage> messages;
    private Boolean stream;
    private Double temperature;
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    public OpenAiChatRequestBody(String model, List<ChatMessage> messages, Boolean stream,
                                 Double temperature, Integer maxTokens) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }
}

package com.acanx.meta.sdk.llm.model;

import java.util.ArrayList;
import java.util.List;

public class ChatRequest {

    private String model;
    private List<ChatMessage> messages = new ArrayList<>();
    private Boolean stream;
    private Double temperature;
    private Integer maxTokens;

    public static Builder builder(String model) {
        return new Builder(model);
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

    public static class Builder {
        private final ChatRequest request = new ChatRequest();

        private Builder(String model) {
            request.setModel(model);
        }

        public Builder message(ChatMessage message) {
            request.getMessages().add(message);
            return this;
        }

        public Builder messages(List<ChatMessage> messages) {
            request.setMessages(messages);
            return this;
        }

        public Builder stream(Boolean stream) {
            request.setStream(stream);
            return this;
        }

        public Builder temperature(Double temperature) {
            request.setTemperature(temperature);
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            request.setMaxTokens(maxTokens);
            return this;
        }

        public ChatRequest build() {
            return request;
        }
    }
}

package com.acanx.meta.model.deepseek.ri;

import com.acanx.meta.model.deepseek.model.Message;
import java.util.ArrayList;
import java.util.List;


public class DeepSeekRi {

    private String model;
    private Boolean stream;
    public double temperature;
    /**
     *  max_tokens  模型单次回答的最大长度（含思维连输出）
     */
    public int maxTokens;
    private List<Message> messages = new ArrayList<>();

    public DeepSeekRi(String model, Boolean stream, List<Message> messages) {
        this.model = model;
        this.stream = stream;
        this.messages = messages;
    }

    public DeepSeekRi(String model, Boolean stream, double temperature, int max_tokens, List<Message> messages) {
        this.model = model;
        this.stream = stream;
        this.temperature = temperature;
        this.maxTokens = max_tokens;
        this.messages = messages;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

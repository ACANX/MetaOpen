package com.acanx.meta.model.deepseek.model;

/**
 * Choice
 *
 */
public class Choice {


    private Integer index;

    private Message message;

    /**
     *   序列化后的字段：finish_reason
     */
    private String finishReason;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}

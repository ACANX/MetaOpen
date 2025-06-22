package com.acanx.meta.model.test.annotation.model;

import java.util.List;

/**
 * MessageFlex
 *
 * @author ACANX
 * @since 202506
 */
public class MessageFlex {
    private Long id;
    private String messageContent;
    private int priorityLevel;
    private List<String> tags;
    private String remark;

    public Long getId() { return id; }
    public String getMessageContent() { return messageContent; }
    public int getPriorityLevel() { return priorityLevel; }
    public List<String> getTags() { return tags; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

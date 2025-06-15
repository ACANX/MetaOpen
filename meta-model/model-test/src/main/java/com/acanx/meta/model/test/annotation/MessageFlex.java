package com.acanx.meta.model.test.annotation;

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

    public Long getId() { return id; }
    public String getMessageContent() { return messageContent; }
    public int getPriorityLevel() { return priorityLevel; }
    public List<String> getTags() { return tags; }
}

package com.acanx.meta.model.test.annotation;

import java.util.List;

/**
 * MessageStable
 *
 * @author ACANX
 * @since 202506
 */
public class MessageStable {
    private String content;
    private int priority;
    private List<String> options;

    public void setContent(String content) { this.content = content; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setOptions(List<String> options) { this.options = options; }
}

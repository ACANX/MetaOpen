package com.acanx.meta.model.test.annotation.model;

import java.util.List;

/**
 * MessageStable
 *
 * @author ACANX
 * @since 202506
 */
public class MessageStable {
    private Long id;
    private String content;
    private int priority;
    private List<String> options;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) { this.content = content; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getContent() {
        return content;
    }

    public int getPriority() {
        return priority;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

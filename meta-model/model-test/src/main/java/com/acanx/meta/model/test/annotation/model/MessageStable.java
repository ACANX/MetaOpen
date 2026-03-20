package com.acanx.meta.model.test.annotation.model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MessageStable that)) return false;
        return getPriority() == that.getPriority() && Objects.equals(getId(), that.getId()) && Objects.equals(getContent(), that.getContent()) && Objects.equals(getOptions(), that.getOptions()) && Objects.equals(getRemark(), that.getRemark());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getPriority(), getOptions(), getRemark());
    }

    @Override
    public String toString() {
        return "MessageStable{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", priority=" + priority +
                ", options=" + options +
                ", remark='" + remark + '\'' +
                '}';
    }
}

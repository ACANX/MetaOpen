package com.acanx.meta.model.test.annotation.model;

import java.util.List;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MessageFlex that)) return false;
        return getPriorityLevel() == that.getPriorityLevel() && Objects.equals(getId(), that.getId()) && Objects.equals(getMessageContent(), that.getMessageContent()) && Objects.equals(getTags(), that.getTags()) && Objects.equals(getRemark(), that.getRemark());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMessageContent(), getPriorityLevel(), getTags(), getRemark());
    }

    @Override
    public String toString() {
        return "MessageFlex{" +
                "id=" + id +
                ", messageContent='" + messageContent + '\'' +
                ", priorityLevel=" + priorityLevel +
                ", tags=" + tags +
                ", remark='" + remark + '\'' +
                '}';
    }


}

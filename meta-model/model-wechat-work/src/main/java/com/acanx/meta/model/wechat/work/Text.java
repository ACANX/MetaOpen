package com.acanx.meta.model.wechat.work;



import java.util.List;

/**
 * Text
 *
 */

public class Text {

    private String content;

    //    @JSONField(name ="mentioned_list")
    private List<String> mentionedList;

    //    @JSONField(name ="mentioned_mobile_list")
    private List<String> mentionedMobileList;

    public Text() {
    }

    public Text(String content, List<String> mentionedMobileList) {
        this.content = content;
        this.mentionedMobileList = mentionedMobileList;
    }

    public Text(String content, List<String> mentionedList, List<String> mentionedMobileList) {
        this.content = content;
        this.mentionedList = mentionedList;
        this.mentionedMobileList = mentionedMobileList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMentionedList() {
        return mentionedList;
    }

    public void setMentionedList(List<String> mentionedList) {
        this.mentionedList = mentionedList;
    }

    public List<String> getMentionedMobileList() {
        return mentionedMobileList;
    }

    public void setMentionedMobileList(List<String> mentionedMobileList) {
        this.mentionedMobileList = mentionedMobileList;
    }

    @Override
    public String toString() {
        return "Text{" +
                "content='" + content + '\'' +
                ", mentionedList=" + mentionedList +
                ", mentionedMobileList=" + mentionedMobileList +
                '}';
    }
}

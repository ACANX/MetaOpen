package com.acanx.meta.model.wechat.work;



/**
 * ChatMessage
 *
 */
public class ChatMessage {

    private String msgtype;

    private Text text;


    private Markdown markdown;

    private News news;

    public ChatMessage(Text text) {
        this.msgtype = "text";
        this.text = text;
    }

    public ChatMessage(Markdown markdown) {
        this.markdown = markdown;
        this.msgtype = "markdown";
    }

    public ChatMessage(News news) {
        this.news = news;
        this.msgtype = "news";
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Markdown getMarkdown() {
        return markdown;
    }

    public void setMarkdown(Markdown markdown) {
        this.markdown = markdown;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "msgtype='" + msgtype + '\'' +
                ", text=" + text +
                ", markdown=" + markdown +
                ", news=" + news +
                '}';
    }
}

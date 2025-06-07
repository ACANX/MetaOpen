package com.acanx.meta.model.wechat.work;

/**
 * Markdown
 *
 */
public class Markdown {

    private String content;

    public Markdown(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Markdown{" +
                "content='" + content + '\'' +
                '}';
    }
}

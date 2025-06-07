package com.acanx.meta.model.dingtalk;

/**
 * SendMessage
 *
 */
public class SendMessage {

    private String msgtype;

    private Text text;

    private At at;


    public SendMessage() {
    }

    public SendMessage(String msgtype, Text text, At at) {
        this.msgtype = msgtype;
        this.text = text;
        this.at = at;
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

    public At getAt() {
        return at;
    }

    public void setAt(At at) {
        this.at = at;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "msgtype='" + msgtype + '\'' +
                ", text=" + text +
                ", at=" + at +
                '}';
    }
}

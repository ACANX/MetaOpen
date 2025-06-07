package com.acanx.meta.service.wechat.work;

import com.acanx.meta.model.wechat.work.Markdown;
import com.acanx.meta.model.wechat.work.News;
import com.acanx.meta.model.wechat.work.Text;

public interface GroupChatMessageService {


    /**
     *  发送Text消息
     * @param text
     * @return
     */
    boolean sendTextMessage(Text text);


    /**
     * 发送News消息
     * @param news
     * @return
     */
    boolean sendNewsMessage(News news);

    /**
     *  发送Markdown消息
     * @param md
     * @return
     */
    boolean sendMarkdownMessage(Markdown md);
}

package com.acanx.meta.service.wechat.work;

import com.acanx.meta.model.wechat.work.Markdown;
import com.acanx.meta.model.wechat.work.News;
import com.acanx.meta.model.wechat.work.Text;

public interface GroupChatMessageService {


    /**
     *  发送Text消息
     *
     * @param text  纯文本消息
     * @return 发送结果
     */
    boolean sendTextMessage(Text text);


    /**
     * 发送News消息
     *
     * @param news  新闻类型的消息
     * @return  发送结果
     */
    boolean sendNewsMessage(News news);

    /**
     *  发送Markdown消息
     *
     * @param md  Markdown消息
     * @return  发送结果
     */
    boolean sendMarkdownMessage(Markdown md);
}

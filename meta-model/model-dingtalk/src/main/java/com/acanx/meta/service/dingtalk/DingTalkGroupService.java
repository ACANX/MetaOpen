package com.acanx.meta.service.dingtalk;

import com.acanx.meta.model.dingtalk.SendMessage;

/**
 * DingTalkGroupService
 *
 */
public interface DingTalkGroupService {

    /**
     *
     * @param message  消息体
     * @return 消息发送结果
     */
    Boolean sendMessage(SendMessage message);

}

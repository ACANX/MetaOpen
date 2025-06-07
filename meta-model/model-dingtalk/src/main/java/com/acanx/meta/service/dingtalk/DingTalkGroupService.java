package com.acanx.meta.service.dingtalk;

import com.acanx.meta.model.dingtalk.SendMessage;

/**
 * DingTalkGroupService
 *
 */
public interface DingTalkGroupService {

    Boolean sendMessage(SendMessage message);

}

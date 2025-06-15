package com.acanx.meta.model.deepseek.builder;

import com.acanx.meta.model.deepseek.c.DeepSeekC;
import com.acanx.meta.model.deepseek.enums.DeepSeekModel;
import com.acanx.meta.model.deepseek.model.Message;
import com.acanx.meta.model.deepseek.ri.DeepSeekRi;

import java.util.ArrayList;
import java.util.List;

/**
 * DeepSeekRiBuilder
 *
 */
public class DeepSeekRiBuilder {

    /**
     *  构件DeepSeek大模型API接口的请求体
     *
     *  https://api-docs.deepseek.com/zh-cn/
     *  https://api-docs.deepseek.com/zh-cn/guides/reasoning_model
     *
     * @param model    模型
     * @param prompt   提示词
     * @param queryString  问题内容描述
     * @return         构建的HTTP请求体Java入参对象
     */
    public static DeepSeekRi getDefaultDeepSeekRi(DeepSeekModel model, String prompt, String queryString) {
        if (model.equals(DeepSeekModel.R1)) {
            return getDefaultDeepSeekR1Ri(prompt, queryString);
        }
        return getDefaultDeepSeekChatRi(prompt, queryString);
    }

    /**
     *   DeepSeek-Chat模型
     *
     * @param prompt   提示词
     * @param queryString  问题内容描述
     * @return  构建的HTTP请求体Java入参对象
     */
    public static DeepSeekRi getDefaultDeepSeekChatRi(String prompt, String queryString) {
        List<Message> msgList = new ArrayList<>();
        msgList.add(new Message(DeepSeekC.SYSTEM, prompt));
        msgList.add(new Message(DeepSeekC.USER, queryString));
        DeepSeekRi ri = new DeepSeekRi(DeepSeekModel.CHAT.getModel(), false, msgList);
        ri.setTemperature(1.0D);
        ri.setMaxTokens(8 * 1024);
        return ri;
    }

    /**
     *  DeepSeek-Reasoner模型
     *
     * @param prompt  提示词
     * @param queryString 问题内容描述
     * @return  构建的HTTP请求体Java入参对象
     */
    public static DeepSeekRi getDefaultDeepSeekR1Ri(String prompt, String queryString) {
        List<Message> msgList = new ArrayList<>();
        msgList.add(new Message(DeepSeekC.SYSTEM, prompt));
        msgList.add(new Message(DeepSeekC.USER, queryString));
        DeepSeekRi ri = new DeepSeekRi(DeepSeekModel.R1.getModel(), false, msgList);
        // https://api-docs.deepseek.com/zh-cn/guides/reasoning_model#%E8%AE%BF%E9%97%AE%E6%A0%B7%E4%BE%8B
        // ri.setTemperature(1.0D);
        ri.setMaxTokens(32 * 1024);
        return ri;
    }


}

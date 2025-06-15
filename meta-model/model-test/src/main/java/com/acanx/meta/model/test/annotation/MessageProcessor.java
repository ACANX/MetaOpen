package com.acanx.meta.model.test.annotation;

import com.acanx.annotation.FieldMapping;
import com.acanx.annotation.List;
import com.acanx.annotation.ObjectCopy;

/**
 * MessageProcessor
 *
 * @author ACANX
 * @since 202506
 */
public class MessageProcessor {
    /**
     * 基本用法
     * @param source   源
     * @param target  目标
     */
    @ObjectCopy
    public void copyBasic(MessageFlex source, MessageStable target) {
        // 编译时将被替换
    }

    /**
     * 高级配置
     * @param origin 源
     * @param target 目标
     */
    @ObjectCopy(
            copyNulls = true,
            ignoreFields = {"id", "internalCode"},
            fieldMappings = {
                    @ObjectCopy.FieldMapping(source = "messageContent", target = "content"),
                    @ObjectCopy.FieldMapping(source = "priorityLevel", target = "priority")
            }
    )
    public void convertMessageToChoice(MessageFlex origin, MessageStable target) {
        // 编译时将被替换
    }

    /**
     * 多个注解
     * @param source  源
     * @param target  目标
     */
    @ObjectCopy.List({
            @ObjectCopy(ignoreFields = "id"),
            @ObjectCopy(fieldMappings = @ObjectCopy.FieldMapping(source = "tags", target = "options"))
    })
    public void copyWithMultipleAnnotations(MessageFlex source, MessageStable target) {
        // 编译时将被替换
    }


    /**
     *
     * @param message  源
     */
    public void process(MessageFlex message) {
        MessageStable choice = new MessageStable();
        convertMessageToChoice(message, choice);
        System.out.println("Copied choice: " + choice);
    }
}

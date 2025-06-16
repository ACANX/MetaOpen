package com.acanx.meta.model.test.annotation;

import com.acanx.annotation.ObjectCopy;
import com.acanx.meta.model.test.json.model.User;

import java.time.LocalDateTime;

/**
 * MessageProcessor
 *
 * @author ACANX
 * @since 202506
 */
public class MessageProcessor {
//    /**
//     * 基本用法
//     * @param source   源
//     * @param target  目标
//     */
//    @ObjectCopy
//    public void copyBasic(MessageFlex source, MessageStable target) {
//        // 编译时将被替换
//    }
//
//    /**
//     * 高级配置
//     * @param origin 源
//     * @param target 目标
//     */
//    @ObjectCopy(
//            copyNulls = true,
//            ignoreFields = {"id", "internalCode"},
//            fieldMappings = {
//                    @ObjectCopy.FieldMapping(source = "messageContent", target = "content"),
//                    @ObjectCopy.FieldMapping(source = "priorityLevel", target = "priority")
//            }
//    )
//    public void convertMessageToChoice(MessageFlex origin, MessageStable target) {
//        // 编译时将被替换
//    }
//
//    /**
//     * 多个注解
//     * @param source  源
//     * @param target  目标
//     */
//    @ObjectCopy.List({
//            @ObjectCopy(ignoreFields = "id"),
//            @ObjectCopy(fieldMappings = @ObjectCopy.FieldMapping(source = "tags", target = "options"))
//    })
//    public void copyWithMultipleAnnotations(MessageFlex source, MessageStable target) {
//        // 编译时将被替换
//    }


    /**
     *
     * @param source
     * @param target
     */
    @ObjectCopy(
            copyNulls = false,
            ignoreFields = {"password"},
            fieldMappings = {
                    @ObjectCopy.FieldMapping(source = "userName", target = "loginId"),
                    @ObjectCopy.FieldMapping(source = "email", target = "contactEmail")
            }
    )
    public void copyUser(User source, UserDTO target) {
        // 编译期生成的代码将放在辅助类中
        // UserCopierHelper.copy(source, target);
    }

//    /**
//     *
//     * @param message  源
//     */
//    public void process(MessageFlex message) {
//        MessageStable choice = new MessageStable();
//        convertMessageToChoice(message, choice);
//        System.out.println("Copied choice: " + choice);
//    }

    public static void main(String[] args) {
        User user = new User(1011,"ACC", LocalDateTime.now());
        user.setEmail("abc@gmail.com");
        user.setPassword("123456");
        UserDTO userDTO = new UserDTO();
        MessageProcessor processor = new MessageProcessor();
        processor.copyUser(user, userDTO);
        System.out.println(userDTO.toString());
    }


}

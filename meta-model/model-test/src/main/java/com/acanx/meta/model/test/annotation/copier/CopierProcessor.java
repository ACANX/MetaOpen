package com.acanx.meta.model.test.annotation.copier;

import com.acanx.meta.model.test.annotation.copier.copier.MessageCopier;
import com.acanx.meta.model.test.annotation.copier.copier.UserCopier;
import com.acanx.meta.model.test.annotation.model.MessageFlex;
import com.acanx.meta.model.test.annotation.model.MessageStable;
import com.acanx.meta.model.test.json.model.User;
import com.acanx.meta.model.test.json.model.UserDTO;
import com.acanx.util.annotation.Copier;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * CopierProcessor
 *
 * @author ACANX
 * @since 202506
 */
public class CopierProcessor {

    /**
     * 单纯的注解
     * {@link MessageCopier#convertMessageFlexToMessageStable(MessageFlex, MessageStable)}
     *
     * @param source 源
     * @param target 目标
     */
    @Copier
    public void convertMessageFlexToMessageStable(MessageFlex source, MessageStable target) {
        // 转换实现由 @Copier 注解处理器在编译期生成
    }

    /**
     * 用户对象转换
     *
     * @param source 源
     * @param target 目标
     */
    @Copier
    public void convertUserToUserDTO(User source, UserDTO target) {
        // 转换实现由 @Copier 注解处理器在编译期生成
    }

    /**
     * 带自定义规则的对象拷贝
     *
     * @param source 源
     * @param target 目标
     */
    @Copier(ignoreNull = false)
    void convertUserToUserDTOWithIgnorePassword(User source, UserDTO target) {
        // 转换实现由 @Copier 注解处理器在编译期生成
    }

    /**
     * 处理函数
     *
     * @param message 源
     */
    public void process(MessageFlex message) {
        MessageStable choice = new MessageStable();
        MessageCopier.convertMessageFlexToMessageStable(message, choice);
        System.out.println("Copied choice: " + choice);
    }

    /**
     * 测试方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        User user = new User(1011, "ACE", LocalDateTime.now(ZoneId.systemDefault()));
        user.setEmail("abc@gmail.com");
        user.setPassword("123456");
        System.out.println(user);

        UserDTO userDTO = new UserDTO();
        UserCopier.convertUserToUserDTO(user, userDTO);
        System.out.println(userDTO);

        UserDTO userDTO2 = new UserDTO();
        UserCopier.convertUserToUserDTOWithIgnorePassword(user, userDTO2);
        System.out.println(userDTO2);
    }
}

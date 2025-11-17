package com.acanx.meta.model.test.object.copy;

import com.acanx.meta.model.test.annotation.model.MessageFlex;
import com.acanx.meta.model.test.annotation.model.MessageStable;
import com.acanx.meta.model.test.object.copier.MessageCopier;
import com.acanx.meta.model.test.object.copier.UserCopier;
import com.acanx.meta.model.test.json.model.UserDTO;
import com.acanx.meta.model.test.json.model.User;
import com.acanx.util.incubator.annotation.Copier;

import java.time.LocalDateTime;

/**
 * MessageProcessor
 *
 * @author ACANX
 * @since 202506
 */
public class CopierProcessor {

    /**
     *  单纯的注解
     *  {@link MessageCopier#convertMessageFlexToMessageStable(MessageFlex, MessageStable)}
     *
     * @param source   源
     * @param target   目标
     */
    @Copier
    public void convertMessageFlexToMessageStable(MessageFlex source, MessageStable target) {}


    /**
     *   用户对象转换
     */
    @Copier
    public void convertUserToUserDTO(User source, UserDTO target) {
        // 编译期生成的代码将放在辅助类中
        // UserCopierHelper.copy(source, target);
    }

//    /**
//     *  带自定义规则的对象拷贝
//     *
//     * @param source 源
//     * @param target 目标
//     */
//    @ObjectCopy(
//            copyNulls = false,
//            ignoreFields = {"password"},
//            fieldMappings = {
//                    @ObjectCopy.FieldMapping(s = "userName", t = "loginId"),
//                    @ObjectCopy.FieldMapping(s = "email", t = "contactEmail")
//            }
//    )
//    void convertUserToUserDTOWithIgnorePassword(User source, UserDTO target) {
//        // 编译期生成的代码将放在辅助类中
//        // UserCopierHelper.copy(source, target);
//    }



    /**
     *    处理函数
     *
     * @param message  源
     */
    public void process(MessageFlex message) {
        MessageStable choice = new MessageStable();
        MessageCopier.convertMessageFlexToMessageStable(message, choice);
        System.out.println("Copied choice: " + choice);
    }


    /**
     *   测试方法
     *
     * @param args  命令行参数
     */
    public static void main(String[] args) {
        MessageCopier processor = new MessageCopier();
        UserCopier copier = new UserCopier();

        User user = new User(1011,"ACE", LocalDateTime.now());
        user.setEmail("abc@gmail.com");
        user.setPassword("123456");
        System.out.println(user.toString());

        UserDTO userDTO = new UserDTO();
        copier.convertUserToUserDTO(user, userDTO);
        System.out.println(userDTO.toString());

        UserDTO userDTO2 = new UserDTO();
        copier.convertUserToUserDTOWithIgnorePassword(user, userDTO2);
        System.out.println(userDTO2.toString());
    }


}

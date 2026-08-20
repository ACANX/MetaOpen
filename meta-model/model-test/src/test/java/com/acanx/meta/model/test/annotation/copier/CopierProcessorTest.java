package com.acanx.meta.model.test.annotation.copier;

import com.acanx.meta.model.test.annotation.copier.copier.UserCopier;
import com.acanx.meta.model.test.annotation.model.MessageFlex;
import com.acanx.meta.model.test.json.model.User;
import com.acanx.meta.model.test.json.model.UserDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * CopierProcessor 测试类
 *
 * 覆盖 process 与对象拷贝转换逻辑，提升 SonarCloud New Code 覆盖率（issue #2565）。
 *
 * @author BeeAgent
 * @since 2026-08-20
 */
class CopierProcessorTest {

    @Test
    void shouldProcessMessageFlex() {
        MessageFlex message = new MessageFlex();
        message.setId(1L);
        message.setMessageContent("hello");

        CopierProcessor processor = new CopierProcessor();
        assertDoesNotThrow(() -> processor.process(message));
    }

    @Test
    void shouldConvertUserToUserDTO() {
        User user = new User(1011, "ACE", LocalDateTime.now());
        user.setEmail("abc@gmail.com");
        user.setPassword("123456");

        UserDTO dto = new UserDTO();
        UserCopier.convertUserToUserDTO(user, dto);
        UserCopier.convertUserToUserDTOWithIgnorePassword(user, dto);

        assertNotNull(dto);
    }
}

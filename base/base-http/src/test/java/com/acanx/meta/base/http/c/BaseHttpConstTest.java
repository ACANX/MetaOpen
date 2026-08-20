package com.acanx.meta.base.http.c;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * BaseHttpConst 常量类测试
 *
 * 覆盖 UTF-8 拼接常量的静态初始化逻辑。
 *
 * @author BeeAgent
 * @since 2026-08-20
 */
class BaseHttpConstTest {

    @Test
    void shouldContainUtf8SuffixedMediaTypes() {
        assertEquals("application/json; charset=UTF-8", BaseHttpConst.MEDIA_TYPE_APPLICATION_JSON_UTF8);
        assertEquals("text/plain; charset=UTF-8", BaseHttpConst.MEDIA_TYPE_TEXT_PLAIN_UTF8);
        assertEquals("text/html; charset=UTF-8", BaseHttpConst.MEDIA_TYPE_TEXT_HTML_UTF8);
    }
}

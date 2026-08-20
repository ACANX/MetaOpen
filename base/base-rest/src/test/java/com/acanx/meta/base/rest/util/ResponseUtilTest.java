package com.acanx.meta.base.rest.util;

import com.acanx.meta.base.rest.RestResult;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseUtilTest {

    @Test
    void isSuccessShouldReturnTrueWhenCodeIsOk() {
        RestResult<String> result = RestResult.ok("data");
        assertTrue(ResponseUtil.isSuccess(result), "OK_CODE 应视为成功");
    }

    @Test
    void isSuccessShouldReturnTrueWhenCodeIs200() {
        RestResult<String> result = new RestResult<>(200, "success", null);
        assertTrue(ResponseUtil.isSuccess(result), "200 应视为成功");
    }

    @Test
    void isSuccessShouldReturnTrueWhenCodeIs202() {
        RestResult<String> result = new RestResult<>(202, "accepted", null);
        assertTrue(ResponseUtil.isSuccess(result), "202 应视为成功");
    }

    @Test
    void isSuccessShouldReturnFalseForOtherCodes() {
        RestResult<String> result = RestResult.fail(500, "internal error");
        assertFalse(ResponseUtil.isSuccess(result), "非成功码应返回 false");
    }

    @Test
    void isSuccessShouldReturnFalseForNullCode() {
        RestResult<String> result = new RestResult<>();
        assertFalse(ResponseUtil.isSuccess(result), "code 为 null 应返回 false");
    }

    @Test
    void isSuccessShouldReturnFalseForNullResult() {
        assertFalse(ResponseUtil.isSuccess(null), "result 为 null 应返回 false");
    }

    @Test
    void privateConstructorShouldBeInvokable() throws Exception {
        Constructor<ResponseUtil> constructor = ResponseUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance(), "私有构造器应可被反射调用");
    }
}

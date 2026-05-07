package com.acanx.meta.base.rest;

import com.acanx.meta.base.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * RestResult
 *
 * 统一REST API响应对象
 *
 * @param <T> 数据类型
 * @author ACANX
 * @since 0.1.5
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功状态码
     */
    public static final int OK_CODE = 1;
    /**
     * 成功消息
     */
    public static final String OK_MESSAGE = "操作成功";

    /**
     * 业务错误码（来自 ErrorCode）
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;


    public RestResult() {
    }
    
    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 消息
     * @param data    数据
     */
    public RestResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码枚举
     * @param data      数据
     */
    public RestResult(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 成功响应（无数据）
     */
    public static <T> RestResult<T> ok() {
        return new RestResult<>(OK_CODE, OK_MESSAGE, null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> RestResult<T> ok(T data) {
        return new RestResult<>(OK_CODE, OK_MESSAGE, data);
    }

    /**
     * 成功响应（带数据和消息）
     */
    public static <T> RestResult<T> ok(T data, String message) {
        return new RestResult<>(OK_CODE, message, data);
    }

    /**
     * 失败响应（使用错误码枚举）
     */
    public static <T> RestResult<T> fail(ErrorCode errorCode) {
        return new RestResult<>(errorCode, null);
    }

    /**
     * 失败响应（使用错误码枚举和自定义消息）
     */
    public static <T> RestResult<T> fail(ErrorCode errorCode, String message) {
        RestResult<T> result = new RestResult<>(errorCode, null);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（使用错误码和消息）
     */
    public static <T> RestResult<T> fail(int code, String message) {
        return new RestResult<>(code, message, null);
    }

    /**
     * 根据条件返回成功或失败
     */
    public static <T> RestResult<T> result(boolean success, T data, ErrorCode errorCode) {
        return success ? ok(data) : fail(errorCode);
    }

    // ==================== Getter/Setter ====================

    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

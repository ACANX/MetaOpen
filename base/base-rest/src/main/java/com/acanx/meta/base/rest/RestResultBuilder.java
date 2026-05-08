package com.acanx.meta.base.rest;

import com.acanx.meta.base.error.ErrorCode;

/**
 * RestResultBuilder
 *
 * RestResult 建造者模式工具类
 *
 * @param <T> 数据类型
 * @author ACANX
 * @since 0.1.5
 */
public class RestResultBuilder<T> {

    /**
     * 业务错误码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 无参构造函数
     */
    RestResultBuilder() {
    }

    /**
     * 构造函数
     *
     * @param data 数据
     */
    RestResultBuilder(T data) {
        this.data = data;
    }

    /**
     * 构造函数
     *
     * @param data    数据
     * @param code    错误码
     * @param message 消息
     */
    RestResultBuilder(T data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    /**
     * 创建 Builder
     */
    public static <T> RestResultBuilder<T> builder() {
        return new RestResultBuilder<>();
    }

    /**
     * 创建 Builder（带数据）
     */
    public static <T> RestResultBuilder<T> builder(T data) {
        return new RestResultBuilder<>(data);
    }

    /**
     * 创建默认成功 Builder
     */
    public static <T> RestResultBuilder<T> defaultBuilder(T data) {
        return new RestResultBuilder<>(data, RestResult.OK_CODE, RestResult.OK_MESSAGE);
    }

    /**
     * 创建默认失败 Builder
     */
    public static <T> RestResultBuilder<T> failBuilder(ErrorCode errorCode) {
        return new RestResultBuilder<>(null, errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 设置错误码
     */
    public RestResultBuilder<T> code(int code) {
        this.code = code;
        return this;
    }

    /**
     * 设置错误码（使用枚举）
     */
    public RestResultBuilder<T> code(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        if (this.message == null) {
            this.message = errorCode.getMessage();
        }
        return this;
    }

    /**
     * 设置消息
     */
    public RestResultBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置数据
     */
    public RestResultBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 构建成功结果
     */
    public RestResultBuilder<T> ok() {
        this.code = RestResult.OK_CODE;
        return this;
    }

    /**
     * 构建成功结果（带消息）
     */
    public RestResultBuilder<T> ok(String message) {
        this.code = RestResult.OK_CODE;
        this.message = message;
        return this;
    }

    /**
     * 构建失败结果
     */
    public RestResultBuilder<T> fail(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        return this;
    }

    /**
     * 构建失败结果（带自定义消息）
     */
    public RestResultBuilder<T> fail(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
        return this;
    }

    /**
     * 构建 RestResult
     */
    public RestResult<T> build() {
        return new RestResult<>(this.code, this.message, this.data);
    }

    @Override
    public String toString() {
        return "RestResultBuilder{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
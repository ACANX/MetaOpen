package com.acanx.meta.base.error;

/**
 * ErrorCode
 *
 * 错误码接口，所有错误码定义需实现此接口
 *
 * @author ACANX
 * @since 0.1.5
 */
public interface ErrorCode {

    /**
     * 获取错误码
     *
     * @return 错误码（纯数字）
     */
    int getCode();

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    String getMessage();

    /**
     * 获取HTTP状态码
     *
     * @return HTTP状态码
     */
    int getStatus();

}
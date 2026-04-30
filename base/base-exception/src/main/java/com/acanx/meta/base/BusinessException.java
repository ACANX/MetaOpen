package com.acanx.meta.base;

import java.util.HashMap;
import java.util.Map;

/**
 * BusinessException
 *
 * 业务异常类，用于业务逻辑中可预见的异常
 *
 * @author ACANX
 * @since 0.1.5
 */
public class BusinessException extends BaseException {

    /**
     * 业务模块（可选，用于定位异常来源）
     */
    private String module;

    /**
     * 业务操作（可选，用于定位具体操作）
     */
    private String operation;

    /**
     * 错误详情（可选，用于结构化错误信息）
     * 例：字段级校验错误、批量操作失败详情
     */
    private Map<String, Object> details;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BusinessException(String code, String message, Object... args) {
        super(code, message, args);
    }

    public BusinessException(String code, String message, Throwable cause, Object... args) {
        super(code, message, cause, args);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    /**
     * 设置业务模块（链式调用）
     *
     * @param module 业务模块
     * @return 当前异常对象
     */
    public BusinessException module(String module) {
        this.module = module;
        return this;
    }

    /**
     * 设置业务操作（链式调用）
     *
     * @param operation 业务操作
     * @return 当前异常对象
     */
    public BusinessException operation(String operation) {
        this.operation = operation;
        return this;
    }

    /**
     * 添加错误详情（链式调用）
     *
     * @param key   键
     * @param value 值
     * @return 当前异常对象
     */
    public BusinessException addDetail(String key, Object value) {
        if (details == null) {
            details = new HashMap<>();
        }
        details.put(key, value);
        return this;
    }

    /**
     * 批量设置错误详情（链式调用）
     *
     * @param details 错误详情Map
     * @return 当前异常对象
     */
    public BusinessException withDetails(Map<String, Object> details) {
        this.details = details;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BusinessException{");
        if (getCode() != null) {
            sb.append("code='").append(getCode()).append("'");
        }
        if (getMessage() != null) {
            if (getCode() != null) {
                sb.append(", ");
            }
            sb.append("message='").append(getMessage()).append("'");
        }
        if (getArgs() != null && getArgs().length > 0) {
            sb.append(", args=").append(java.util.Arrays.toString(getArgs()));
        }
        if (module != null) {
            sb.append(", module='").append(module).append("'");
        }
        if (operation != null) {
            sb.append(", operation='").append(operation).append("'");
        }
        if (details != null && !details.isEmpty()) {
            sb.append(", details=").append(details);
        }
        sb.append("}");
        return sb.toString();
    }
}

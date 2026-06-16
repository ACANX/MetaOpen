package com.acanx.meta.base.exception;

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
    private final String module;

    /**
     * 业务操作（可选，用于定位具体操作）
     */
    private final String operation;

    /**
     * 错误详情（可选，用于结构化错误信息）
     * 例：字段级校验错误、批量操作失败详情
     */
    private final Map<String, Object> details;

    public BusinessException() {
        super();
        this.module = null;
        this.operation = null;
        this.details = null;
    }

    public BusinessException(String message) {
        super(message);
        this.module = null;
        this.operation = null;
        this.details = null;
    }

    public BusinessException(String code, String message) {
        super(code, message);
        this.module = null;
        this.operation = null;
        this.details = null;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
        this.module = null;
        this.operation = null;
        this.details = null;
    }

    public BusinessException(String code, String message, Object... args) {
        super(code, message, args);
        this.module = null;
        this.operation = null;
        this.details = null;
    }

    public BusinessException(String code, String message, Throwable cause, Object... args) {
        super(code, message, cause, args);
        this.module = null;
        this.operation = null;
        this.details = null;
    }

    public String getModule() {
        return module;
    }

    public String getOperation() {
        return operation;
    }

    public Map<String, Object> getDetails() {
        return details;
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

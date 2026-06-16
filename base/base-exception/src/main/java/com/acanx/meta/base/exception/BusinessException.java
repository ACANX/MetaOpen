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

    private BusinessException(Builder builder) {
        super(builder.code, builder.message, builder.args);
        this.module = builder.module;
        this.operation = builder.operation;
        this.details = builder.details;
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

    /**
     * 创建 BusinessException 构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * BusinessException 构建器
     */
    public static class Builder {
        private String code;
        private String message;
        private Object[] args;
        private String module;
        private String operation;
        private Map<String, Object> details;

        Builder() {
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder args(Object... args) {
            this.args = args;
            return this;
        }

        /**
         * 设置业务模块
         *
         * @param module 业务模块
         * @return 构建器
         */
        public Builder module(String module) {
            this.module = module;
            return this;
        }

        /**
         * 设置业务操作
         *
         * @param operation 业务操作
         * @return 构建器
         */
        public Builder operation(String operation) {
            this.operation = operation;
            return this;
        }

        /**
         * 添加错误详情
         *
         * @param key   键
         * @param value 值
         * @return 构建器
         */
        public Builder addDetail(String key, Object value) {
            if (this.details == null) {
                this.details = new HashMap<>();
            }
            this.details.put(key, value);
            return this;
        }

        /**
         * 批量设置错误详情
         *
         * @param details 错误详情Map
         * @return 构建器
         */
        public Builder withDetails(Map<String, Object> details) {
            this.details = details;
            return this;
        }

        public BusinessException build() {
            return new BusinessException(this);
        }
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

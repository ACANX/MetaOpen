package com.acanx.meta.base.exception;

/**
 * BaseException
 *
 * 基础异常类，所有自定义异常的基类
 *
 * @author ACANX
 * @since 0.1.5
 */
public class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 国际化参数（高度通用，用于消息模板替换）
     * 例：消息模板"用户名【{0}】已存在" → args=["张三"]
     */
    private final Object[] args;

    public BaseException() {
        super();
        this.code = null;
        this.message = null;
        this.args = null;
    }

    public BaseException(String message) {
        super(message);
        this.code = null;
        this.message = message;
        this.args = null;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = null;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.args = null;
    }

    public BaseException(String code, String message, Object... args) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = args;
    }

    public BaseException(String code, String message, Throwable cause, Object... args) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("{");
        if (code != null) {
            sb.append("code='").append(code).append("'");
        }
        if (message != null) {
            if (code != null) {
                sb.append(", ");
            }
            sb.append("message='").append(message).append("'");
        }
        if (args != null && args.length > 0) {
            sb.append(", args=").append(java.util.Arrays.toString(args));
        }
        sb.append("}");
        return sb.toString();
    }
}

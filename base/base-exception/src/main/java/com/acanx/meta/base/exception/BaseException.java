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
    private String code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 国际化参数（高度通用，用于消息模板替换）
     * 例：消息模板"用户名【{0}】已存在" → args=["张三"]
     */
    private Object[] args;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
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

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
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

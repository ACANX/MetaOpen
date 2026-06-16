package com.acanx.meta.base.exception;

/**
 * SystemException
 *
 * 系统异常类，用于系统级不可恢复的错误
 *
 * <p>适用场景：
 * <ul>
 *   <li>JVM资源耗尽（OOM、线程池满）</li>
 *   <li>基础设施故障（数据库连接池耗尽、缓存服务不可用）</li>
 *   <li>配置加载失败</li>
 *   <li>系统启动失败</li>
 * </ul>
 *
 * <p>特点：
 * <ul>
 *   <li>通常不可恢复，需要运维介入</li>
 *   <li>需要告警通知</li>
 *   <li>对用户隐藏具体原因，返回通用错误提示</li>
 * </ul>
 *
 * @author ACANX
 * @since 0.1.5
 */
public class SystemException extends BaseException {

    /**
     * 是否需要告警（默认true）
     */
    private final boolean alert;

    /**
     * 是否可恢复（默认false）
     */
    private final boolean recoverable;

    public SystemException() {
        super();
        this.alert = true;
        this.recoverable = false;
    }

    public SystemException(String message) {
        super(message);
        this.alert = true;
        this.recoverable = false;
    }

    public SystemException(String code, String message) {
        super(code, message);
        this.alert = true;
        this.recoverable = false;
    }

    public SystemException(String code, String message, Throwable cause) {
        super(code, message, cause);
        this.alert = true;
        this.recoverable = false;
    }

    public SystemException(String code, String message, Object... args) {
        super(code, message, args);
        this.alert = true;
        this.recoverable = false;
    }

    public SystemException(String code, String message, Throwable cause, Object... args) {
        super(code, message, cause, args);
        this.alert = true;
        this.recoverable = false;
    }

    public boolean isAlert() {
        return alert;
    }

    public boolean isRecoverable() {
        return recoverable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SystemException{");
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
        sb.append(", alert=").append(alert);
        sb.append(", recoverable=").append(recoverable);
        sb.append("}");
        return sb.toString();
    }
}

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
    private boolean alert = true;

    /**
     * 是否可恢复（默认false）
     */
    private boolean recoverable = false;

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String code, String message) {
        super(code, message);
    }

    public SystemException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public SystemException(String code, String message, Object... args) {
        super(code, message, args);
    }

    public SystemException(String code, String message, Throwable cause, Object... args) {
        super(code, message, cause, args);
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isRecoverable() {
        return recoverable;
    }

    public void setRecoverable(boolean recoverable) {
        this.recoverable = recoverable;
    }

    /**
     * 设置是否需要告警（链式调用）
     *
     * @param alert 是否需要告警
     * @return 当前异常对象
     */
    public SystemException alert(boolean alert) {
        this.alert = alert;
        return this;
    }

    /**
     * 设置是否可恢复（链式调用）
     *
     * @param recoverable 是否可恢复
     * @return 当前异常对象
     */
    public SystemException recoverable(boolean recoverable) {
        this.recoverable = recoverable;
        return this;
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
package com.acanx.meta.base;

import java.time.LocalDateTime;

/**
 * TaskException
 *
 * 任务异常类，用于任务调度相关错误
 *
 * <p>适用场景：
 * <ul>
 *   <li>定时任务执行失败</li>
 *   <li>异步任务超时</li>
 *   <li>任务调度失败</li>
 *   <li>任务状态异常</li>
 * </ul>
 *
 * <p>特点：
 * <ul>
 *   <li>包含任务执行上下文信息</li>
 *   <li>支持重试标识</li>
 *   <li>支持记录执行时间点</li>
 * </ul>
 *
 * @author ACANX
 * @since 0.1.5
 */
public class TaskException extends FrameworkException {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型（如：SCHEDULED、ASYNC、DELAYED）
     */
    private String taskType;

    /**
     * 是否可重试
     */
    private boolean retryable = false;

    /**
     * 已重试次数
     */
    private int retryCount = 0;

    /**
     * 最大重试次数
     */
    private int maxRetryCount = 0;

    /**
     * 任务执行时间
     */
    private LocalDateTime executeTime;

    public TaskException() {
        super();
    }

    public TaskException(String message) {
        super(message);
    }

    public TaskException(String code, String message) {
        super(code, message);
    }

    public TaskException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public TaskException(String code, String message, Object... args) {
        super(code, message, args);
    }

    public TaskException(String code, String message, Throwable cause, Object... args) {
        super(code, message, cause, args);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    /**
     * 设置任务ID（链式调用）
     *
     * @param taskId 任务ID
     * @return 当前异常对象
     */
    public TaskException taskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    /**
     * 设置任务名称（链式调用）
     *
     * @param taskName 任务名称
     * @return 当前异常对象
     */
    public TaskException taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    /**
     * 设置任务类型（链式调用）
     *
     * @param taskType 任务类型
     * @return 当前异常对象
     */
    public TaskException taskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    /**
     * 设置是否可重试（链式调用）
     *
     * @param retryable 是否可重试
     * @return 当前异常对象
     */
    public TaskException retryable(boolean retryable) {
        this.retryable = retryable;
        return this;
    }

    /**
     * 设置重试次数信息（链式调用）
     *
     * @param retryCount    已重试次数
     * @param maxRetryCount 最大重试次数
     * @return 当前异常对象
     */
    public TaskException retryInfo(int retryCount, int maxRetryCount) {
        this.retryCount = retryCount;
        this.maxRetryCount = maxRetryCount;
        return this;
    }

    /**
     * 设置任务执行时间（链式调用）
     *
     * @param executeTime 任务执行时间
     * @return 当前异常对象
     */
    public TaskException executeTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TaskException{");
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
        if (taskId != null) {
            sb.append(", taskId='").append(taskId).append("'");
        }
        if (taskName != null) {
            sb.append(", taskName='").append(taskName).append("'");
        }
        if (taskType != null) {
            sb.append(", taskType='").append(taskType).append("'");
        }
        sb.append(", retryable=").append(retryable);
        if (maxRetryCount > 0) {
            sb.append(", retryCount=").append(retryCount).append("/").append(maxRetryCount);
        }
        if (executeTime != null) {
            sb.append(", executeTime=").append(executeTime);
        }
        sb.append("}");
        return sb.toString();
    }
}

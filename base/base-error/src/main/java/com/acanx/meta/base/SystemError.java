package com.acanx.meta.base;

/**
 * SystemError
 *
 * 系统级错误码定义
 *
 * @author ACANX
 * @since 0.1.5
 */
public enum SystemError implements ErrorCode {

    // ==================== 系统核心错误 3001-3099 ====================
    /**
     * 系统内部错误 (3001)
     */
    SYSTEM_ERROR(3001, "系统内部错误", 500),

    /**
     * 系统繁忙 (3002)
     */
    SYSTEM_BUSY(3002, "系统繁忙，请稍后再试", 503),

    /**
     * 服务不可用 (3003)
     */
    SERVICE_UNAVAILABLE(3003, "服务暂时不可用", 503),

    /**
     * 服务超时 (3004)
     */
    SERVICE_TIMEOUT(3004, "服务请求超时", 504),

    /**
     * 系统配置错误 (3005)
     */
    CONFIG_ERROR(3005, "系统配置错误", 500),

    /**
     * 系统初始化失败 (3006)
     */
    SYSTEM_INIT_FAILED(3006, "系统初始化失败", 500),

    /**
     * 系统维护中 (3007)
     */
    SYSTEM_MAINTENANCE(3007, "系统维护中，请稍后再试", 503),

    // ==================== 资源错误 3101-3199 ====================
    /**
     * 内存不足 (3101)
     */
    OUT_OF_MEMORY(3101, "系统内存不足", 500),

    /**
     * 磁盘空间不足 (3102)
     */
    DISK_FULL(3102, "磁盘空间不足", 500),

    /**
     * 连接池耗尽 (3103)
     */
    CONNECTION_POOL_EXHAUSTED(3103, "连接池资源耗尽", 503),

    /**
     * 线程池耗尽 (3104)
     */
    THREAD_POOL_EXHAUSTED(3104, "线程池资源耗尽", 503),

    /**
     * 文件句柄耗尽 (3105)
     */
    FILE_HANDLE_EXHAUSTED(3105, "文件句柄资源耗尽", 500),

    // ==================== 模块/组件错误 3201-3299 ====================
    /**
     * 模块加载失败 (3201)
     */
    MODULE_LOAD_FAILED(3201, "模块加载失败", 500),

    /**
     * 模块未找到 (3202)
     */
    MODULE_NOT_FOUND(3202, "模块未找到", 404),

    /**
     * 模块初始化失败 (3203)
     */
    MODULE_INIT_FAILED(3203, "模块初始化失败", 500),

    /**
     * 组件异常 (3204)
     */
    COMPONENT_ERROR(3204, "组件运行异常", 500),

    /**
     * 插件加载失败 (3205)
     */
    PLUGIN_LOAD_FAILED(3205, "插件加载失败", 500),

    // ==================== 安全错误 3301-3399 ====================
    /**
     * 安全漏洞检测 (3301)
     */
    SECURITY_VIOLATION(3301, "检测到安全违规", 403),

    /**
     * 访问被拒绝 (3302)
     */
    ACCESS_DENIED(3302, "访问被拒绝", 403),

    /**
     * 签名验证失败 (3303)
     */
    SIGNATURE_ERROR(3303, "签名验证失败", 401),

    /**
     * 证书无效 (3304)
     */
    CERTIFICATE_INVALID(3304, "证书无效", 401),

    /**
     * 加密解密失败 (3305)
     */
    CRYPTO_ERROR(3305, "加密解密操作失败", 500),

    // ==================== 依赖服务错误 3401-3499 ====================
    /**
     * 数据库服务异常 (3401)
     */
    DATABASE_ERROR(3401, "数据库服务异常", 500),

    /**
     * 缓存服务异常 (3402)
     */
    CACHE_ERROR(3402, "缓存服务异常", 500),

    /**
     * 消息队列异常 (3403)
     */
    MQ_ERROR(3403, "消息队列服务异常", 500),

    /**
     * 注册中心异常 (3404)
     */
    REGISTRY_ERROR(3404, "注册中心服务异常", 500),

    /**
     * 配置中心异常 (3405)
     */
    CONFIG_CENTER_ERROR(3405, "配置中心服务异常", 500),

    // ==================== 运行时错误 3501-3599 ====================
    /**
     * 空指针异常 (3501)
     */
    NULL_POINTER(3501, "空指针异常", 500),

    /**
     * 类型转换异常 (3502)
     */
    TYPE_CAST_ERROR(3502, "类型转换异常", 500),

    /**
     * 数组越界 (3503)
     */
    ARRAY_INDEX_OUT_OF_BOUNDS(3503, "数组索引越界", 500),

    /**
     * 参数非法 (3504)
     */
    ILLEGAL_ARGUMENT(3504, "非法参数", 400),

    /**
     * 状态非法 (3505)
     */
    ILLEGAL_STATE(3505, "非法状态", 400),

    /**
     * 操作不支持 (3506)
     */
    UNSUPPORTED_OPERATION(3506, "不支持的操作", 500),

    // ==================== 网络/通信错误 3601-3699 ====================
    /**
     * 网络连接失败 (3601)
     */
    NETWORK_ERROR(3601, "网络连接失败", 500),

    /**
     * 请求超时 (3602)
     */
    REQUEST_TIMEOUT(3602, "请求超时", 504),

    /**
     * 连接被拒绝 (3603)
     */
    CONNECTION_REFUSED(3603, "连接被拒绝", 500),

    /**
     * 连接重置 (3604)
     */
    CONNECTION_RESET(3604, "连接被重置", 500),

    /**
     * DNS解析失败 (3605)
     */
    DNS_RESOLVE_ERROR(3605, "DNS解析失败", 500),

    // ==================== 未知错误 ====================
    /**
     * 未知错误 (3999)
     */
    UNKNOWN_ERROR(3999, "未知错误", 500);

    /**
     * 错误码（纯数字）
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * HTTP状态码
     */
    private final int status;

    SystemError(int code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    /**
     * 根据错误码查找枚举
     *
     * @param code 错误码
     * @return 错误码枚举，未找到返回UNKNOWN_ERROR
     */
    public static SystemError fromCode(int code) {
        for (SystemError errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }

    @Override
    public String toString() {
        return "SystemError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}

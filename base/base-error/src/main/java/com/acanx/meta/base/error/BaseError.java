package com.acanx.meta.base.error;

/**
 * BaseErrorCode
 *
 * 基础框架层面的通用错误码定义
 *
 * @author ACANX
 * @since 0.1.5
 */
public enum BaseError implements ErrorCode {

    // ==================== 成功状态 ====================
    /**
     * 成功 (0)
     */
    SUCCESS(0, "操作成功", 200),


    /**
     * 认证失败 (401)
     */
    AUTH_FAILED(401, "认证失败", 401),
    /**
     * 请求方法不支持 (405)
     */
    METHOD_NOT_SUPPORTED(405, "请求方法不支持", 405),
    /**
     * 请求路径不存在 (404)
     */
    PATH_NOT_FOUND(404, "请求路径不存在", 404),
    /**
     * 数据已存在 (409)
     */
    DATA_ALREADY_EXISTS(409, "数据已存在", 409),
    /**
     * 请求内容类型不支持 (415)
     */
    CONTENT_TYPE_NOT_SUPPORTED(415, "请求内容类型不支持", 415),
    /**
     * 资源锁定 (423)
     */
    RESOURCE_LOCKED(423, "资源已被锁定", 423),
    /**
     * API调用限流 (429)
     */
    API_RATE_LIMIT(429, "API调用频率超限", 429),
    /**
     * 未知错误 (999)
     */
    UNKNOWN_ERROR(999, "未知错误", 500),





    // ==================== 系统级错误 1001-1099 ====================
    /**
     * 系统内部错误 (1001)
     */
    SYSTEM_ERROR(1001, "系统内部错误", 500),

    /**
     * 系统繁忙 (1002)
     */
    SYSTEM_BUSY(1002, "系统繁忙，请稍后再试", 503),

    /**
     * 服务不可用 (1003)
     */
    SERVICE_UNAVAILABLE(1003, "服务暂时不可用", 503),

    /**
     * 服务超时 (1004)
     */
    SERVICE_TIMEOUT(1004, "服务请求超时", 504),

    /**
     * 配置错误 (1005)
     */
    CONFIG_ERROR(1005, "系统配置错误", 500),

    // ==================== 参数校验错误 1101-1199 ====================
    /**
     * 参数缺失 (1101)
     */
    PARAM_MISSING(1101, "缺少必要参数", 400),

    /**
     * 参数格式错误 (1102)
     */
    PARAM_FORMAT_ERROR(1102, "参数格式错误", 400),

    /**
     * 参数值无效 (1103)
     */
    PARAM_INVALID(1103, "参数值无效", 400),

    /**
     * 参数范围错误 (1104)
     */
    PARAM_RANGE_ERROR(1104, "参数范围超出限制", 400),

    /**
     * 参数类型错误 (1105)
     */
    PARAM_TYPE_ERROR(1105, "参数类型错误", 400),

    // ==================== 请求错误 1201-1299 ====================
    /**
     * 请求体解析失败 (1201)
     */
    REQUEST_BODY_PARSE_ERROR(1201, "请求体解析失败", 400),

    /**
     * 请求头缺失 (1202)
     */
    HEADER_MISSING(1202, "缺少必要的请求头", 400),

    // ==================== 数据错误 1301-1399 ====================
    /**
     * 数据不存在 (1301)
     */
    DATA_NOT_FOUND(1301, "数据不存在", 404),

    /**
     * 数据格式错误 (1302)
     */
    DATA_FORMAT_ERROR(1302, "数据格式错误", 400),

    /**
     * 数据校验失败 (1303)
     */
    DATA_VALIDATION_ERROR(1303, "数据校验失败", 400),

    /**
     * 数据操作失败 (1304)
     */
    DATA_OPERATION_ERROR(1304, "数据操作失败", 500),

    // ==================== 权限认证错误 1401-1499 ====================
    /**
     * 未登录 (1401)
     */
    UNAUTHORIZED(1401, "用户未登录", 401),
    /**
     * 无权限 (1402)
     */
    FORBIDDEN(1402, "无操作权限", 403),
    /**
     * 登录已过期 (1403)
     */
    TOKEN_EXPIRED(1403, "登录已过期", 401),

    /**
     * Token无效 (1404)
     */
    TOKEN_INVALID(1404, "Token无效", 401),
    /**
     * 账号被禁用 (1405)
     */
    ACCOUNT_DISABLED(1405, "账号已被禁用", 403),


    // ==================== 业务错误 1501-1599 ====================
    /**
     * 业务处理失败 (1501)
     */
    BUSINESS_ERROR(1501, "业务处理失败", 400),

    /**
     * 业务规则校验失败 (1502)
     */
    BUSINESS_RULE_ERROR(1502, "业务规则校验失败", 400),

    /**
     * 操作不允许 (1503)
     */
    OPERATION_NOT_ALLOWED(1503, "当前操作不允许", 400),


    // ==================== 文件错误 1601-1699 ====================
    /**
     * 文件不存在 (1601)
     */
    FILE_NOT_FOUND(1601, "文件不存在", 404),

    /**
     * 文件上传失败 (1602)
     */
    FILE_UPLOAD_ERROR(1602, "文件上传失败", 500),

    /**
     * 文件下载失败 (1603)
     */
    FILE_DOWNLOAD_ERROR(1603, "文件下载失败", 500),

    /**
     * 文件格式不支持 (1604)
     */
    FILE_FORMAT_NOT_SUPPORTED(1604, "文件格式不支持", 400),

    /**
     * 文件大小超限 (1605)
     */
    FILE_SIZE_EXCEEDED(1605, "文件大小超出限制", 400),

    // ==================== 网络/远程调用错误 1701-1799 ====================
    /**
     * 远程服务调用失败 (1701)
     */
    REMOTE_SERVICE_ERROR(1701, "远程服务调用失败", 500),

    /**
     * 网络连接失败 (1702)
     */
    NETWORK_ERROR(1702, "网络连接失败", 500);




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

    BaseError(int code, String message, int status) {
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
    public static BaseError fromCode(int code) {
        for (BaseError errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }

    @Override
    public String toString() {
        return "BaseErrorCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}

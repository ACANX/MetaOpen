package com.acanx.meta.base.rest.util;

import com.acanx.meta.base.rest.RestResult;

/**
 * ResponseUtil
 *
 * @author ACANX
 * @since 20260506
 */
public class ResponseUtil {

    /**
     * 成功状态码（与服务端 RestResult.OK_CODE 一致）
     */
    private static final int SUCCESS_CODE = 1;

    public static boolean isSuccess(RestResult<?> result) {
        return result != null && null != result.getCode() && (result.getCode() == SUCCESS_CODE || result.getCode() == 200 || result.getCode() == 202);
    }

}
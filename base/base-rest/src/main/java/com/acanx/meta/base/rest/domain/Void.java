package com.acanx.meta.base.rest.domain;

/**
 * Void
 *
 * @author ACANX
 * @since 20260429
 */
public class Void {

    public Void() {
        // 空响应类型（如 RestResult<Void>），Jackson 反序列化需要默认无参构造器，不可删除（SonarCloud java:S1186 豁免）
    }
}
package com.acanx.meta.model.trans;

import java.math.BigDecimal;

/**
 * TransSecuMin
 *
 * @author ACANX
 * @since 20250926
 */
public class TransSecuMin {


    /**
     *  时间戳
     */
    private Long ts;
    /**
     *  市场
     */
    private String m;

    /**
     *  证券代码
     */
    private String s;

    /**
     *  开
     */
    private BigDecimal o;
    /**
     *  关
     */
    private BigDecimal c;

    /**
     *  高
     */
    private BigDecimal h;

    /**
     *  低
     */
    private BigDecimal l;

    /**
     *  当日累计均价
     */
    private BigDecimal ad;

    /**
     *  成交量
     */
    private BigDecimal v;
    /**
     *  成交额
     */
    private BigDecimal t;
    /**
     *  扩展字段
     */
    private String e;

    public TransSecuMin() {
    }

    public TransSecuMin(String s, String m, Long ts) {
        this.s = s;
        this.m = m;
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "TransSecuMin{" +
                "ts=" + ts +
                ", m='" + m + '\'' +
                ", s='" + s + '\'' +
                ", o=" + o +
                ", c=" + c +
                ", h=" + h +
                ", l=" + l +
                ", ad=" + ad +
                ", v=" + v +
                ", t=" + t +
                ", e='" + e + '\'' +
                '}';
    }
}

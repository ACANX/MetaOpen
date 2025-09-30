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

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public BigDecimal getO() {
        return o;
    }

    public void setO(BigDecimal o) {
        this.o = o;
    }

    public BigDecimal getC() {
        return c;
    }

    public void setC(BigDecimal c) {
        this.c = c;
    }

    public BigDecimal getH() {
        return h;
    }

    public void setH(BigDecimal h) {
        this.h = h;
    }

    public BigDecimal getL() {
        return l;
    }

    public void setL(BigDecimal l) {
        this.l = l;
    }

    public BigDecimal getAd() {
        return ad;
    }

    public void setAd(BigDecimal ad) {
        this.ad = ad;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }

    public BigDecimal getT() {
        return t;
    }

    public void setT(BigDecimal t) {
        this.t = t;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }
}

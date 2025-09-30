package com.acanx.meta.model.rss;



import java.math.BigDecimal;
import java.util.Objects;

/**
 *   分钟级别行情数据模型
 *
 */
public class SecurityMinute {

    private Long ts;

    private Integer tradeDate;

    private Integer tradeTime;

    private String marketCode;

    private String securityCode;

    private BigDecimal priceOpen;

    private BigDecimal priceClose;

    private BigDecimal priceHigh;

    private BigDecimal priceLow;

    private BigDecimal priceAvgCurrDay;

    private BigDecimal volume;

    private BigDecimal turnover;

    private String extField;


    public SecurityMinute() {
    }


    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Integer getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Integer tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Integer tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public BigDecimal getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(BigDecimal priceOpen) {
        this.priceOpen = priceOpen;
    }

    public BigDecimal getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(BigDecimal priceClose) {
        this.priceClose = priceClose;
    }

    public BigDecimal getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(BigDecimal priceHigh) {
        this.priceHigh = priceHigh;
    }

    public BigDecimal getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(BigDecimal priceLow) {
        this.priceLow = priceLow;
    }

    public BigDecimal getPriceAvgCurrDay() {
        return priceAvgCurrDay;
    }

    public void setPriceAvgCurrDay(BigDecimal priceAvgCurrDay) {
        this.priceAvgCurrDay = priceAvgCurrDay;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public String getExtField() {
        return extField;
    }

    public void setExtField(String extField) {
        this.extField = extField;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SecurityMinute that = (SecurityMinute) object;
        return Objects.equals(getTs(), that.getTs()) && Objects.equals(getTradeDate(), that.getTradeDate()) && Objects.equals(getTradeTime(), that.getTradeTime()) && Objects.equals(getMarketCode(), that.getMarketCode()) && Objects.equals(getSecurityCode(), that.getSecurityCode()) && Objects.equals(getPriceOpen(), that.getPriceOpen()) && Objects.equals(getPriceClose(), that.getPriceClose()) && Objects.equals(getPriceHigh(), that.getPriceHigh()) && Objects.equals(getPriceLow(), that.getPriceLow()) && Objects.equals(getPriceAvgCurrDay(), that.getPriceAvgCurrDay()) && Objects.equals(getVolume(), that.getVolume()) && Objects.equals(getTurnover(), that.getTurnover()) && Objects.equals(getExtField(), that.getExtField());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getTs());
        result = 31 * result + Objects.hashCode(getTradeDate());
        result = 31 * result + Objects.hashCode(getTradeTime());
        result = 31 * result + Objects.hashCode(getMarketCode());
        result = 31 * result + Objects.hashCode(getSecurityCode());
        result = 31 * result + Objects.hashCode(getPriceOpen());
        result = 31 * result + Objects.hashCode(getPriceClose());
        result = 31 * result + Objects.hashCode(getPriceHigh());
        result = 31 * result + Objects.hashCode(getPriceLow());
        result = 31 * result + Objects.hashCode(getPriceAvgCurrDay());
        result = 31 * result + Objects.hashCode(getVolume());
        result = 31 * result + Objects.hashCode(getTurnover());
        result = 31 * result + Objects.hashCode(getExtField());
        return result;
    }

    @Override
    public String toString() {
        return "SecurityMinute{" +
                "ts=" + ts +
                ", tradeDate=" + tradeDate +
                ", tradeTime=" + tradeTime +
                ", marketCode='" + marketCode + '\'' +
                ", securityCode='" + securityCode + '\'' +
                ", priceOpen=" + priceOpen +
                ", priceClose=" + priceClose +
                ", priceHigh=" + priceHigh +
                ", priceLow=" + priceLow +
                ", priceAvgCurrDay=" + priceAvgCurrDay +
                ", volume=" + volume +
                ", turnover=" + turnover +
                ", extField='" + extField + '\'' +
                '}';
    }
}

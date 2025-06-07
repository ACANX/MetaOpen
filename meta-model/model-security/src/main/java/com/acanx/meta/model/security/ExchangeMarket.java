package com.acanx.meta.model.security;

import java.util.Objects;

/**
 * ExchangeMarket
 *
 */
public class ExchangeMarket {

    private String region;

    private String market;

    private String marketCode;

    private String marketName;
    /**
     *  市场/交易所简称
     */
    private String marketAbbr;

    private String marketAbbrCn;

    private String timezone;
    /**
     *  币种
     */
    private String currencyType;


    public ExchangeMarket() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketAbbr() {
        return marketAbbr;
    }

    public void setMarketAbbr(String marketAbbr) {
        this.marketAbbr = marketAbbr;
    }

    public String getMarketAbbrCn() {
        return marketAbbrCn;
    }

    public void setMarketAbbrCn(String marketAbbrCn) {
        this.marketAbbrCn = marketAbbrCn;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ExchangeMarket that = (ExchangeMarket) object;
        return Objects.equals(getMarketCode(), that.getMarketCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMarketCode());
    }

    @Override
    public String toString() {
        return "ExchangeMarket{" +
                "region='" + region + '\'' +
                ", market='" + market + '\'' +
                ", marketCode='" + marketCode + '\'' +
                ", marketName='" + marketName + '\'' +
                ", marketAbbr='" + marketAbbr + '\'' +
                ", marketAbbrCn='" + marketAbbrCn + '\'' +
                ", timezone='" + timezone + '\'' +
                ", currencyType='" + currencyType + '\'' +
                '}';
    }
}

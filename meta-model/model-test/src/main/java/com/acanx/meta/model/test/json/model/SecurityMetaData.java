package com.acanx.meta.model.test.json.model;

import java.util.Objects;

/**
 * SecurityMetaData
 *
 */
public class SecurityMetaData {

    private String symbol;

    private String securityName;

    private String market;

    private String region;

    private String securityType;

    private Integer yearIpo;

    private Double currPe;

    private Double currPb;

    private Double currPrice;

    public SecurityMetaData() {
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public Integer getYearIpo() {
        return yearIpo;
    }

    public void setYearIpo(Integer yearIpo) {
        this.yearIpo = yearIpo;
    }

    public Double getCurrPe() {
        return currPe;
    }

    public void setCurrPe(Double currPe) {
        this.currPe = currPe;
    }

    public Double getCurrPb() {
        return currPb;
    }

    public void setCurrPb(Double currPb) {
        this.currPb = currPb;
    }

    public Double getCurrPrice() {
        return currPrice;
    }

    public void setCurrPrice(Double currPrice) {
        this.currPrice = currPrice;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SecurityMetaData that = (SecurityMetaData) object;
        return Objects.equals(getSymbol(), that.getSymbol()) && Objects.equals(getSecurityName(), that.getSecurityName()) && Objects.equals(getMarket(), that.getMarket()) && Objects.equals(getRegion(), that.getRegion()) && Objects.equals(getSecurityType(), that.getSecurityType()) && Objects.equals(getYearIpo(), that.getYearIpo()) && Objects.equals(getCurrPe(), that.getCurrPe()) && Objects.equals(getCurrPb(), that.getCurrPb()) && Objects.equals(getCurrPrice(), that.getCurrPrice());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getSymbol());
        result = 31 * result + Objects.hashCode(getSecurityName());
        result = 31 * result + Objects.hashCode(getMarket());
        result = 31 * result + Objects.hashCode(getRegion());
        result = 31 * result + Objects.hashCode(getSecurityType());
        result = 31 * result + Objects.hashCode(getYearIpo());
        result = 31 * result + Objects.hashCode(getCurrPe());
        result = 31 * result + Objects.hashCode(getCurrPb());
        result = 31 * result + Objects.hashCode(getCurrPrice());
        return result;
    }

    @Override
    public String toString() {
        return "SecurityMetaData{" +
                "symbol='" + symbol + '\'' +
                ", securityName='" + securityName + '\'' +
                ", market='" + market + '\'' +
                ", region='" + region + '\'' +
                ", securityType='" + securityType + '\'' +
                ", yearIpo=" + yearIpo +
                ", currPe=" + currPe +
                ", currPb=" + currPb +
                ", currPrice=" + currPrice +
                '}';
    }
}

package com.acanx.meta.model.security;

/**
 * Security
 *
 */
public class Security {

    private String region;

    private String marketCode;

    private String securityCode;

    private String securityName;

    /**
     *  证券分类
     *
     *  - Stock
     *  - StockIndex
     *  - Future
     *  - FutureIndex
     *  - ETF
     *  - OtcETF
     *  - ForeignExchange
     *  - FXCFD  外汇差价合约
     *  - PreciousMetalContract 账户贵金属
     *  - GoldDerivatives 黄金衍生品
     */
    private String securityCategory;


    public Security() {
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public String getSecurityCategory() {
        return securityCategory;
    }

    public void setSecurityCategory(String securityCategory) {
        this.securityCategory = securityCategory;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Security security = (Security) object;
        return getMarketCode().equals(security.getMarketCode()) && getSecurityCode().equals(security.getSecurityCode());
    }

    @Override
    public int hashCode() {
        int result = getMarketCode().hashCode();
        result = 31 * result + getSecurityCode().hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "Security{" +
                "region='" + region + '\'' +
                ", marketCode='" + marketCode + '\'' +
                ", securityCode='" + securityCode + '\'' +
                ", securityName='" + securityName + '\'' +
                ", securityCategory='" + securityCategory + '\'' +
                '}';
    }
}

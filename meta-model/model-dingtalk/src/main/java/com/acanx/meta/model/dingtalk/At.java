package com.acanx.meta.model.dingtalk;

import java.util.List;

/**
 * At
 *
 */
public class At {

    private Boolean isAtAll;

    private List<String> atMobiles;

    private List<String> atUserIds;

    public At() {
    }

    public At(Boolean isAtAll, List<String> atMobiles) {
        this.isAtAll = isAtAll;
        this.atMobiles = atMobiles;
    }

    public At(Boolean isAtAll, List<String> atMobiles, List<String> atUserIds) {
        this.isAtAll = isAtAll;
        this.atMobiles = atMobiles;
        this.atUserIds = atUserIds;
    }

    public Boolean getAtAll() {
        return isAtAll;
    }

    public void setAtAll(Boolean atAll) {
        isAtAll = atAll;
    }

    public List<String> getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }

    public List<String> getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(List<String> atUserIds) {
        this.atUserIds = atUserIds;
    }
}

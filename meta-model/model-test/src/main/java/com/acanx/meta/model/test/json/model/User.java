package com.acanx.meta.model.test.json.model;

import java.time.LocalDateTime;

/**
 * User
 *
 */
public class User {

    private Integer userId;
    private String userName;
    private LocalDateTime createTime;


    public User() {
    }

    public User(Integer userId, String userName, LocalDateTime createTime) {
        this.userId = userId;
        this.userName = userName;
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

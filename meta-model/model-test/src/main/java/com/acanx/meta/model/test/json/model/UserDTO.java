package com.acanx.meta.model.test.json.model;

import java.time.LocalDateTime;

/**
 * UserDTO
 *
 * @author ACANX
 * @since 202506
 */
public class UserDTO {
    private Integer userId;
    private String loginId;
    private String password;
    private String contactEmail;
    private LocalDateTime createTime;

    /**
     * 默认构造函数，用于 JSON 反序列化和框架反射创建实例。
     */
    public UserDTO() {
        // 用于 JSON 反序列化和框架反射创建实例
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", loginId='" + loginId + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

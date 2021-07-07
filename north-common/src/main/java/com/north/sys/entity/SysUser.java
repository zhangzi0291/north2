package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@TableName("sys_user")
public class SysUser extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * EMail
     */
    private String email;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;

    /**
     * 头像URL
     */
    private String iconUrl;

    /**
     * 描述
     */
    private String describe;


    /**
     * 应用名称
     */
    private String appName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysUser.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("orgId='" + orgId + "'")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("nickname='" + nickname + "'")
                .add("phone='" + phone + "'")
                .add("email='" + email + "'")
                .add("status=" + status)
                .add("expiredTime=" + expiredTime)
                .add("iconUrl='" + iconUrl + "'")
                .add("describe='" + describe + "'")
                .add("appName='" + appName + "'")
                .toString();
    }
}

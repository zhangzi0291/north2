package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.north.base.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * <p>
 *
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-09
 */
@TableName("sys_log")
public class SysLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 日志类型,1登录，2退出
     */
    private Integer logType;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * IP
     */
    private String ipAddr;

    /**
     * 备注
     */
    private String remakr;

    /**
     * 执行时间
     */
    private Long useTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getRemakr() {
        return remakr;
    }

    public void setRemakr(String remakr) {
        this.remakr = remakr;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUseTime() {
        return useTime;
    }

    public void setUseTime(Long useTime) {
        this.useTime = useTime;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysLog.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("uesrId='" + userId + "'")
                .add("nickname='" + nickname + "'")
                .add("moduleName='" + moduleName + "'")
                .add("operationName='" + operationName + "'")
                .add("logType=" + logType)
                .add("className='" + className + "'")
                .add("methodName='" + methodName + "'")
                .add("ipAddr='" + ipAddr + "'")
                .add("remakr='" + remakr + "'")
                .add("useTime=" + useTime)
                .add("createdTime=" + createdTime)
                .toString();
    }
}

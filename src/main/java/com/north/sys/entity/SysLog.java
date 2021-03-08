package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

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
    private String uesrId;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUesrId() {
        return uesrId;
    }

    public void setUesrId(String uesrId) {
        this.uesrId = uesrId;
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
    public String toString() {
        return new StringJoiner(", ", SysLog.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("uesrId='" + uesrId + "'")
                .add("nickname='" + nickname + "'")
                .add("moduleName='" + moduleName + "'")
                .add("operationName='" + operationName + "'")
                .add("logType=" + logType)
                .add("className='" + className + "'")
                .add("methodName='" + methodName + "'")
                .add("ipAddr='" + ipAddr + "'")
                .add("remakr='" + remakr + "'")
                .toString();
    }
}

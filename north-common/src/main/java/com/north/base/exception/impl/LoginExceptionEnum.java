package com.north.base.exception.impl;

import com.north.base.exception.INorthException;

public enum LoginExceptionEnum implements INorthException {
    USER_PWD_ERROR(500,"用户名或密码错误"),
    EXPIRED_ERROR(500,"用户过期"),
    LOCKING_ERROR(500,"用户锁定"),
    GEN_ERROR(500,"验证码校验失败"),
    ;

    final int httpCode = 500;
    final int code;
    final String msg;

    LoginExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public int getHttpCode() {
        return this.httpCode;
    }

    @Override
    public Object getData() {
        return null;
    }

}

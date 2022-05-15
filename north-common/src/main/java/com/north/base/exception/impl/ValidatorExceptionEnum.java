package com.north.base.exception.impl;

import com.north.base.exception.INorthException;

public enum ValidatorExceptionEnum implements INorthException {
    VALIDATOR_ERROR(500,"校验失败")
    ;
    final int httpCode = 500;
    final int code;
    final String msg;

    ValidatorExceptionEnum(int code, String msg) {
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

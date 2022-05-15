package com.north.base.exception.impl;

import com.north.base.exception.INorthException;

public enum CurlExceptionEnum implements INorthException {
    GENERAL_FAILED(500,"未知错误"),
    DELETE_FAILED(500,"删除失败"),
    UPDATE_FAILED(500,"编辑失败"),
    INSERT_FAILED(500,"新增失败"),
    ;

    final int httpCode = 500;
    final int code;
    final String msg;

    CurlExceptionEnum(int code, String msg) {

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

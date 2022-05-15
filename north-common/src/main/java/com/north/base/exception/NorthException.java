package com.north.base.exception;

public class NorthException extends RuntimeException {

    /**
     * 异常消息码
     */
    final int msgCode;

    final int httpCode;

    final Object data;

    public NorthException(int httpCode, int msgCode, String message,Object data) {
        super(message);
        this.httpCode = httpCode;
        this.msgCode = msgCode;
        this.data = data;
    }

    public NorthException(INorthException msgExceptionInfo) {
        this(msgExceptionInfo.getHttpCode(), msgExceptionInfo.getCode(), msgExceptionInfo.getMsg(),msgExceptionInfo.getData());
    }

}

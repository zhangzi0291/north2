package com.north.base.api;

public enum ApiErrorCode implements IErrorCode {

    /**
     * 失败
     */
    FAILED(500, "操作失败"),
    /**
     * 成功
     */
    SUCCESS(200, "执行成功"),
    /**
     * 未登陆
     */
    UNAUTHORIZED(401, "未登录"),
    /**
     * 无权限
     */
    FORBIDDEN(403, "无权限访问"),
    /**
     * 字段校验失败
     */
    CheckFieldError(40001, "字段校验失败"),

    ;
    private final long code;
    private final String msg;

    ApiErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static ApiErrorCode fromCode(long code) {
        ApiErrorCode[] ecs = ApiErrorCode.values();
        for (ApiErrorCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }

}
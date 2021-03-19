package com.north.base.exception;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-23
 */
public class ValidatorException extends NorthBaseException {

    public static final String FAILED_MSG = "字段校验失败";

    public ValidatorException() {
        super(FAILED_MSG);
    }

    public ValidatorException(String message) {
        super(message);
    }
}

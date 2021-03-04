package com.north.base.exception.curd;

import com.north.base.exception.NorthBaseException;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
public class InsertFailedException extends NorthBaseException {

    public static final String FAILED_MSG = "新增失败";

    public InsertFailedException() {
        super(FAILED_MSG);
    }

    public InsertFailedException(String message) {
        super(message);
    }
}

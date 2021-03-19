package com.north.base.exception.curd;

import com.north.base.exception.NorthBaseException;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
public class UpdateFailedException extends NorthBaseException {

    public static final String FAILED_MSG = "编辑失败";

    public UpdateFailedException() {
        super(FAILED_MSG);
    }

    public UpdateFailedException(String message) {
        super(message);
    }
}

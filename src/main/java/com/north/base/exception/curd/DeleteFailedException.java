package com.north.base.exception.curd;

import com.north.base.exception.NorthBaseException;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
public class DeleteFailedException extends NorthBaseException {

    public static final String FAILED_MSG = "删除失败";

    public DeleteFailedException() {
        super(FAILED_MSG);
    }

    public DeleteFailedException(String message) {
        super(message);
    }
}

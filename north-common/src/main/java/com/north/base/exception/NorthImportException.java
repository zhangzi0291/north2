package com.north.base.exception;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-29
 */
public class NorthImportException extends NorthBaseException {

    public static final String FAILED_MSG = "导入失败";

    private ExcelImportResult<Map<String, Object>> importResult;

    public NorthImportException() {
        super(FAILED_MSG);
    }

    public NorthImportException(String message, ExcelImportResult<Map<String, Object>> importResult) {
        super(message);
        this.importResult = importResult;
    }

    public ExcelImportResult<Map<String, Object>> getImportResult() {
        return importResult;
    }
}

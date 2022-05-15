package com.north.base.exception.impl;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.north.base.exception.INorthException;

import java.util.Map;

public enum ImportExceptionEnum implements INorthException {
    IMPORT_ERROR(500,"导入失败")
    ;
    final int httpCode = 500;
    final int code;
    final String msg;
    ExcelImportResult<Map<String, Object>> data;

    ImportExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
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
        return this.data;
    }

    public void setData(ExcelImportResult<Map<String, Object>> data) {
        this.data = data;
    }
}

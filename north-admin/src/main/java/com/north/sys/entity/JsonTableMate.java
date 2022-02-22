package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteBaseModel;

import java.util.StringJoiner;

/**
 * <p>
 * json表结构
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@TableName("json_table_mate")
public class JsonTableMate extends DeleteBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 表id
     */
    private String tableId;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 排序号
     */
    private Integer orderNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JsonTableMate.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("tableId='" + tableId + "'")
                .add("columnName='" + columnName + "'")
                .add("columnType='" + columnType + "'")
                .add("orderNum=" + orderNum)
                .toString();
    }
}

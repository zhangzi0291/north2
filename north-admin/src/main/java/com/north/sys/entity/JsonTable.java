package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

/**
 * <p>
 * json表
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@TableName("json_table")
public class JsonTable extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 表名
     */
    private String tableName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "JsonTable{" +
            "id=" + id +
            ", tableName=" + tableName +
        "}";
    }
}

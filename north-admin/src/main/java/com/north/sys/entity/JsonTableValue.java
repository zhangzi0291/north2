package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteBaseModel;

/**
 * <p>
 * json数据总表
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@TableName("json_table_value")
public class JsonTableValue extends DeleteBaseModel {

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
     * json数据
     */
    private String jsonValue;



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
    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @Override
    public String toString() {
        return "JsonTableValue{" +
            "id=" + id +
            ", tableId=" + tableId +
            ", jsonValue=" + jsonValue +
        "}";
    }
}

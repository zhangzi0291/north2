package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * <p>
 *
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-22
 */
@TableName("sys_dict")
public class SysDict extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 字典KEY
     */
    private String dictKey;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 值类型，1:String,2:Number
     */
    private Integer valueType;

    /**
     * 排序
     */
    private Integer dictOrder;

    /**
     * 扩展字段
     */
    private String dictExt;

    /**
     * 描述
     */
    private String describe;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public Integer getDictOrder() {
        return dictOrder;
    }

    public void setDictOrder(Integer dictOrder) {
        this.dictOrder = dictOrder;
    }

    public String getDictExt() {
        return dictExt;
    }

    public void setDictExt(String dictExt) {
        this.dictExt = dictExt;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysDict.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("dictKey='" + dictKey + "'")
                .add("dictName='" + dictName + "'")
                .add("dictValue='" + dictValue + "'")
                .add("valueType=" + valueType)
                .add("dictOrder=" + dictOrder)
                .add("dictExt='" + dictExt + "'")
                .add("describe='" + describe + "'")
                .toString();
    }
}

package com.north.base;

import org.springframework.util.StringUtils;

import java.util.StringJoiner;

/**
 * 用于前端排序的对象
 *
 * @author Northzx
 * @version 1.0
 * @since 2021-02-25
 */
public class Sort {

    /**
     * 排序方式，ascend正序，descend倒叙
     */
    private String order;

    /**
     * 排序字段名
     */
    private String field;


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean isAsc() {
        if (this.order == null) {
            return true;
        }
        return this.order.toLowerCase().startsWith("asc");
    }

    public Boolean isNeedSort() {
        return StringUtils.hasLength(this.order) && StringUtils.hasLength(this.field);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Sort.class.getSimpleName() + "[", "]")
                .add("order='" + order + "'")
                .add("field='" + field + "'")
                .toString();
    }
}

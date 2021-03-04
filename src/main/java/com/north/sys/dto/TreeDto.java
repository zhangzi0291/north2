package com.north.sys.dto;

import java.util.List;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-29
 */
public class TreeDto<T> {

    private String id;

    private String pid;

    private T data;

    private List<T> child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getChild() {
        return child;
    }

    public void setChild(List<T> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "TreeDto{" +
                "data=" + data +
                ", child=" + child +
                '}';
    }
}

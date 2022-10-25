package com.north.el.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

/**
 * <p>
 * App信息
 * </p>
 *
 * @author NorthZX
 * @since 2022-05-29
 */
@TableName("et_app_info")
public class EtAppInfo extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 图片的id
     */
    private String imgId;

    /**
     * 应用路径
     */
    private String appUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @Override
    public String toString() {
        return "EtAppInfo{" +
            "id=" + id +
            ", appName=" + appName +
            ", imgId=" + imgId +
            ", appUrl=" + appUrl +
        "}";
    }
}

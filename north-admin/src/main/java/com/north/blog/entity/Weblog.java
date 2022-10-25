package com.north.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.north.base.DeleteLogicBaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * <p>
 * 网络日志
 * </p>
 *
 * @author NorthZX
 * @since 2022-01-21
 */
@TableName("weblog")
public class Weblog extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 博客标题
     */
    private String weblogTitle;

    /**
     * 文章文本
     */
    private String weblogText;

    /**
     * 标题图片
     */
    private String weblogTitleImage;

    /**
     * 浏览量
     */
    private Integer pageView;

    /**
     * 发布用户ID
     */
    private String publicUserid;

    /**
     * blog类型
     */
    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否可以编辑
     */
    @TableField(exist=false)
    private Boolean canEdit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getWeblogTitle() {
        return weblogTitle;
    }

    public void setWeblogTitle(String weblogTitle) {
        this.weblogTitle = weblogTitle;
    }
    public String getWeblogText() {
        return weblogText;
    }

    public void setWeblogText(String weblogText) {
        this.weblogText = weblogText;
    }
    public String getWeblogTitleImage() {
        return weblogTitleImage;
    }

    public void setWeblogTitleImage(String weblogTitleImage) {
        this.weblogTitleImage = weblogTitleImage;
    }
    public Integer getPageView() {
        return pageView;
    }

    public void setPageView(Integer pageView) {
        this.pageView = pageView;
    }
    public String getPublicUserid() {
        return publicUserid;
    }

    public void setPublicUserid(String publicUserid) {
        this.publicUserid = publicUserid;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Weblog.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("weblogTitle='" + weblogTitle + "'")
                .add("weblogText='" + weblogText + "'")
                .add("weblogTitleImage='" + weblogTitleImage + "'")
                .add("pageView=" + pageView)
                .add("publicUserid='" + publicUserid + "'")
                .add("type=" + type)
                .add("createdTime=" + createdTime)
                .add("status=" + status)
                .add("canEdit=" + canEdit)
                .toString();
    }
}

package com.north.pytool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.north.base.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author NorthZX
 * @since 2021-07-21
 */
@TableName("pytool_app")
public class PytoolApp extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 软件名称
     */
    private String softName;

    /**
     * 软件版本
     */
    private String softVersion;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 版本更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime versionUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }
    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public LocalDateTime getVersionUpdateTime() {
        return versionUpdateTime;
    }

    public void setVersionUpdateTime(LocalDateTime versionUpdateTime) {
        this.versionUpdateTime = versionUpdateTime;
    }

    @Override
    public String toString() {
        return "PytoolApp{" +
            "id=" + id +
            ", softName=" + softName +
            ", softVersion=" + softVersion +
            ", fileId=" + fileId +
            ", versionUpdateTime=" + versionUpdateTime +
        "}";
    }
}

package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * <p>
 *
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-18
 */
@TableName("sys_file")
public class SysFile extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * MD5
     */
    private String md5Value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }


    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysFile.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("fileName='" + fileName + "'")
                .add("originalName='" + originalName + "'")
                .add("fileSize=" + fileSize)
                .add("filePath='" + filePath + "'")
                .add("uploadTime=" + uploadTime)
                .add("deleteTime=" + deleteTime)
                .add("md5Value='" + md5Value + "'")
                .toString();
    }
}

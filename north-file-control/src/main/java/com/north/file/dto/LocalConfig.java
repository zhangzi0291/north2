package com.north.file.dto;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
public class LocalConfig {

    private String uploadPath;

    public LocalConfig(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LocalConfig.class.getSimpleName() + "[", "]")
                .add("uploadPath='" + uploadPath + "'")
                .toString();
    }
}

package com.north.sys.dto;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-19
 */
public class UploadDto {

    private String fileId;

    private String downloadUrl;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "UploadDto{" +
                "fileId='" + fileId + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}

package com.north.file.dto;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
public class AliyunOssConfig {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String prefixKey;

    public AliyunOssConfig(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String prefixKey) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.prefixKey = prefixKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefixKey() {
        return prefixKey;
    }

    public void setPrefixKey(String prefixKey) {
        this.prefixKey = prefixKey;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AliyunOssConfig.class.getSimpleName() + "[", "]")
                .add("endpoint='" + endpoint + "'")
                .add("accessKeyId='" + accessKeyId + "'")
                .add("accessKeySecret='" + accessKeySecret + "'")
                .add("bucketName='" + bucketName + "'")
                .add("prefixKey='" + prefixKey + "'")
                .toString();
    }
}

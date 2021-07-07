package com.north.file.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.StringUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.north.file.FileControlHandler;
import com.north.file.NorthFileFailedException;
import com.north.file.dto.AliyunOssConfig;

import java.io.InputStream;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
public class AliyunOssFileControlHandler implements FileControlHandler {

    private AliyunOssConfig config;

    private OSS ossClient;

    public AliyunOssFileControlHandler(AliyunOssConfig config) {
        this.config = config;

        checkConfig();
        ossClient = new OSSClientBuilder().build(this.config.getEndpoint(), this.config.getAccessKeyId(), this.config.getAccessKeySecret());
    }

    private void checkConfig() {
        if (StringUtils.isNullOrEmpty(this.config.getEndpoint())) {
            throw new NorthFileFailedException("初始化OSS参数endpoint缺失");
        }
        if (StringUtils.isNullOrEmpty(this.config.getAccessKeyId())) {
            throw new NorthFileFailedException("初始化OSS参数accessKeyId缺失");
        }
        if (StringUtils.isNullOrEmpty(this.config.getAccessKeySecret())) {
            throw new NorthFileFailedException("初始化OSS参数accessKeySecret缺失");
        }
        if (StringUtils.isNullOrEmpty(this.config.getBucketName())) {
            throw new NorthFileFailedException("初始化OSS参数bucketName缺失");
        }
        if (StringUtils.isNullOrEmpty(this.config.getPrefixKey())) {
            throw new NorthFileFailedException("初始化OSS参数prefixKey缺失");
        }
    }

    public String getKey(String filePath) {
        return this.config.getPrefixKey() + "/" + filePath;
    }

    @Override
    public Boolean fileExit(String filePath) {
        String saveKey = getKey(filePath);
        ObjectListing objectListing = ossClient.listObjects(this.config.getBucketName(), saveKey);
        return objectListing.getObjectSummaries().size() > 0;
    }

    @Override
    public void saveFile(InputStream input, String filePath) {
        String saveKey = getKey(filePath);

        ossClient.putObject(this.config.getBucketName(), saveKey, input);
    }

    @Override
    public InputStream loadFileInputStream(String filePath) {
        String saveKey = getKey(filePath);

        OSSObject object = ossClient.getObject(this.config.getBucketName(), saveKey);
        return object.getObjectContent();
    }

    @Override
    public void deleteFile(String filePath) {
        String saveKey = getKey(filePath);

        ossClient.deleteObject(this.config.getBucketName(), saveKey);
    }

}

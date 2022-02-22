package com.north.file.configuration;

import com.north.file.FileControlFactory;
import com.north.file.FileControlHandler;
import com.north.file.dto.AliyunOssConfig;
import com.north.file.dto.FileControlType;
import com.north.file.dto.LocalConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
@Configuration
public class FileStorageConfiguration {

    @Value("${north.file.oss.endpoint:}")
    private String endpoint;

    @Value("${north.file.oss.access-key-id:}")
    private String accessKeyId;

    @Value("${north.file.oss.access-key-secret:}")
    private String accessKeySecret;

    @Value("${north.file.oss.bucket-name:}")
    private String bucketName;

    @Value("${north.file.oss.prefix-key:}")
    private String prefixKey;

    @Value("${north.file.local.upload-path:./upload}")
    private String uploadPath;

    @ConditionalOnProperty(prefix = "north.file", name = "type", havingValue = "local", matchIfMissing = true)
    @Bean
    public FileControlHandler localFileControlHandler() {
        LocalConfig localConfig = getLocalConfig ();
        return FileControlFactory.getFileControlHandler(FileControlType.LOCAL, localConfig);
    }

    @Bean
    public LocalConfig getLocalConfig (){
        return new LocalConfig(uploadPath);
    }

    @ConditionalOnProperty(prefix = "north.file", name = "type", havingValue = "oss")
    @Bean
    public FileControlHandler aliyunOssFileControlHandler() {
        AliyunOssConfig config = getAliyunOssConfig();
        return FileControlFactory.getFileControlHandler(FileControlType.OSS, config);
    }

    @Bean
    public AliyunOssConfig getAliyunOssConfig (){
        return new AliyunOssConfig(endpoint, accessKeyId, accessKeySecret, bucketName, prefixKey);
    }
}

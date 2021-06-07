package com.north.file;

import com.north.file.dto.AliyunOssConfig;
import com.north.file.dto.FileControlType;
import com.north.file.dto.LocalConfig;
import com.north.file.impl.AliyunOssFileControlHandler;
import com.north.file.impl.LocalFileControlHandler;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
public class FileControlFactory {


    /**
     * 获取FileControlHandler
     * @param type
     * @param config FileControlHandler的配置文件，取第一个匹配的
     * @return
     */
    public static FileControlHandler getFileControlHandler(FileControlType type,Object ... config){
        FileControlHandler fileControlHandler;

        switch (type){
            case LOCAL:
                LocalConfig localConfig = null;
                for (Object o : config) {
                    if(o instanceof LocalConfig){
                        localConfig = (LocalConfig)o;
                        break;
                    }
                }
                fileControlHandler = new LocalFileControlHandler(localConfig);
                break;
            case OSS:
                AliyunOssConfig ossConfig = null;
                for (Object o : config) {
                    if(o instanceof AliyunOssConfig){
                        ossConfig = (AliyunOssConfig)o;
                        break;
                    }
                }
                fileControlHandler = new AliyunOssFileControlHandler(ossConfig);
                break;
            default:
                fileControlHandler = new LocalFileControlHandler(new LocalConfig("./upload"));
                break;
        }
        return fileControlHandler;
    }

}

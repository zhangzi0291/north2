package com.north.file;

import java.io.InputStream;
import java.time.LocalDate;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
public interface FileControlHandler {

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    Boolean fileExit(String filePath);

    /**
     * 保存文件，默认覆盖
     *
     * @param input
     * @param filePath
     */
    void saveFile(InputStream input, String filePath);

    /**
     * 保存文件
     * 失败抛出NorthFileFailedException运行时异常
     *
     * @param input
     * @param filePath
     * @param override 是否覆盖原文件，true覆盖，false 已存在抛出NorthFileFailedException运行时异常
     */
    default void saveFile(InputStream input, String filePath, boolean override) {
        if (!override) {
            if (fileExit(filePath)) {
                throw new NorthFileFailedException("源文件已存在");
            }
        }
        saveFile(input, filePath);
    }

    /**
     * 读取文件流
     * 失败抛出NorthFileFailedException运行时异常
     *
     * @param filePath
     * @return
     */
    InputStream loadFileInputStream(String filePath);

    /**
     * 删除文件
     * 失败抛出NorthFileFailedException运行时异常
     *
     * @param filePath
     */
    void deleteFile(String filePath);


    default String getDatePathStr() {
        LocalDate localDate = LocalDate.now();
        String datePath =  localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth()+ "/";
        return datePath;
    }
}

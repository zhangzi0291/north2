package com.north.file.impl;

import com.north.file.FileControlHandler;
import com.north.file.NorthFileFailedException;
import com.north.file.dto.LocalConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-25
 */
public class LocalFileControlHandler implements FileControlHandler {

    private LocalConfig config;

    public LocalFileControlHandler(LocalConfig config) {
        this.config = config;
    }

    @Override
    public Boolean fileExit(String filePath) {
        Path savePath = Paths.get(this.config.getUploadPath(), filePath);
        return Boolean.valueOf(Files.exists(savePath));
    }

    @Override
    public void saveFile(InputStream input, String filePath) {
        Path savePath = Paths.get(this.config.getUploadPath(), filePath);
        try {
            if (Files.notExists(savePath.getParent())) {
                Files.createDirectories(savePath.getParent());
            }
            int index;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream(savePath.toFile());
            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
            input.close();
        } catch (IOException e) {
            throw new NorthFileFailedException("保存失败");
        }
    }


    @Override
    public InputStream loadFileInputStream(String filePath) {
        Path file = Paths.get(this.config.getUploadPath(), filePath);
        if(Files.notExists(file)){
            return null;
        }
        try {
            InputStream inputStream = new FileInputStream(file.toFile());
            return inputStream;
        } catch (IOException e) {
            throw new NorthFileFailedException("读取失败");
        }
    }

    @Override
    public void deleteFile(String filePath) {
        Path savePath = Paths.get(this.config.getUploadPath(), filePath);
        try {
            Files.delete(savePath);
        } catch (IOException e) {
            throw new NorthFileFailedException("删除失败");
        }
    }

}

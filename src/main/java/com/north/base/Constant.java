package com.north.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-30
 */
@Component
public class Constant {

    public static String CONTEXT_PATH;

    @Value("${server.servlet.context-path:}")
    public void setContextPath(String contextPath) {
        CONTEXT_PATH = contextPath;
    }

    public static String UPLOAD_PATH;

    @Value("${north.updown.path:}")
    public void setUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath;
    }
}

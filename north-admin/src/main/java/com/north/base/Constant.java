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

    public static String SYS_MODULE_NAME = "sys";

    public static String GENEALOGY_MODULE_NAME = "genealogy";

    public static String PY_SMALL_TOOL_WEB_URL;

    @Value("${py-small-tool.web-url:http://www.northzx.top:38081/north/#/}")
    public void setPySmallToolWebUrl(String pySmallToolWebUrl) {
        PY_SMALL_TOOL_WEB_URL = pySmallToolWebUrl;
    }

    public static String PY_SMALL_TOOL_SOFTVERSION;

    @Value("${py-small-tool.softversion:1.0}")
    public void setPySmallToolSoftversion(String pySmallToolSoftversion) {
        PY_SMALL_TOOL_SOFTVERSION = pySmallToolSoftversion;
    }

    public static String PY_SMALL_TOOL_SOFTNAME;

    @Value("${py-small-tool.softname:快速预约}")
    public void setPySmallToolSoftname(String pySmallToolSoftname) {
        PY_SMALL_TOOL_SOFTNAME = pySmallToolSoftname;
    }

    public static String AES_KEY;

    @Value("${aes.key:jiannanchuntuand}")
    public void setAesKey(String aesKey) {
        AES_KEY = aesKey;
    }

    public static String AES_IV;

    @Value("${aes.iv:8145933630441549}")
    public void setAesIv(String aesIv) {
        AES_IV = aesIv;
    }


}

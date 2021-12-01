package com.north.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-07-05
 */
public class PasswordUtil {

    public static String encodePassword(String password) {
        String p = SecureUtil.sha1(password);
        return p;
    }

}

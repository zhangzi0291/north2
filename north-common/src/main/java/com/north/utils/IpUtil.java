package com.north.utils;


import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-09
 */
public class IpUtil {

    public static String getIp(HttpServletRequest request) {
        String forwards = request.getHeader("x-forwarded-for");
        if (!StringUtils.hasLength(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasLength(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasLength(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getRemoteAddr();
        }
        if (!StringUtils.hasLength(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getHeader("X-Real-IP");
        }
        if (forwards != null && forwards.trim().length() > 0) {
            int index = forwards.indexOf(',');
            return (index != -1) ? forwards.substring(0, index) : forwards;
        }
        return forwards;
    }

}

package com.north.base.filter;


import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 跨域访问拦截
 *
 * @Author zhengxiangnan
 * @Date 2018/3/8 17:15
 */
public class CorsFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String origin = request.getHeader("ORIGIN");
        String secFetchMode = request.getHeader("Sec-Fetch-Mode");

        if ( origin == null || !"cors".equals(secFetchMode)) {
            filterChain.doFilter(request, response);
        } else {
            response.setHeader("Access-Control-Allow-Origin", origin);//* or origin as u prefer
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "content-type, authorization, x-requested-with");
            if ("OPTIONS".equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                filterChain.doFilter(request, response);
            }
        }

    }

}

package com.north.sys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.north.sys.entity.SysLog;
import com.north.sys.mapper.SysLogMapper;
import com.north.sys.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.utils.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-09
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public void addLoginLog() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysLog log = new SysLog();
        log.setLogType(1);
        log.setUserId(StpUtil.getLoginIdAsString());
        Object nickname = StpUtil.getSession().getAttribute("nickname");
        if(nickname != null ) {
            log.setNickname(nickname.toString());
        }
        log.setIpAddr(IpUtil.getIp(request));
        log.setModuleName("登录");
        log.setOperationName("登录");
        this.save(log);
    }

    @Override
    public void addLogoutLog() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysLog log = new SysLog();
        log.setLogType(2);
        log.setUserId(StpUtil.getLoginIdAsString());
        Object nickname = StpUtil.getSession().getAttribute("nickname");
        if(nickname != null ) {
            log.setNickname(nickname.toString());
        }
        log.setIpAddr(IpUtil.getIp(request));
        log.setModuleName("登录");
        log.setOperationName("注销");
        this.save(log);
    }

    @Override
    public Long getTodayUser() {
        return baseMapper.getTodayUser();
    }
}

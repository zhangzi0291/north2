package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysLog;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-09
 */
public interface ISysLogService extends IService<SysLog> {

    void addLoginLog();

    void addLogoutLog();

    Long getTodayUser();
}

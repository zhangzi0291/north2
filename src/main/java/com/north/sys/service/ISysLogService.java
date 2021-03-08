package com.north.sys.service;

import com.north.sys.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-09
 */
public interface ISysLogService extends IService<SysLog> {

    void addLoginLog();

    void addLogoutLog();

}

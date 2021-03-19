package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-18
 */
public interface ISysFileService extends IService<SysFile> {

    /**
     * 通过MD5查询对应的文件
     *
     * @param md5
     * @return
     */
    SysFile getSysFileByMD5(String md5);

}

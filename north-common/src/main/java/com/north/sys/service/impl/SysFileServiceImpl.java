package com.north.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.entity.SysFile;
import com.north.sys.mapper.SysFileMapper;
import com.north.sys.service.ISysFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-18
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Override
    public SysFile getSysFileByMD5(String md5) {
        LambdaQueryWrapper<SysFile> qw = Wrappers.lambdaQuery();
        qw.eq(SysFile::getMd5Value, md5);
        return this.getOne(qw, true);
    }
}

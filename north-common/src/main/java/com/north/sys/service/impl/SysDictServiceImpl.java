package com.north.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.entity.SysDict;
import com.north.sys.mapper.SysDictMapper;
import com.north.sys.service.ISysDictService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-22
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public Boolean checkDict(String dictName, String dictKey) {
        LambdaQueryWrapper<SysDict> qw = Wrappers.lambdaQuery();
        qw.eq(SysDict::getDictName, dictName);
        qw.eq(SysDict::getDictKey, dictKey);
        List<SysDict> list = this.list(qw);
        return list.size() <= 0;
    }
}

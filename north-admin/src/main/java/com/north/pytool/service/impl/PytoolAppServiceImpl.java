package com.north.pytool.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.pytool.entity.PytoolApp;
import com.north.pytool.mapper.PytoolAppMapper;
import com.north.pytool.service.IPytoolAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.entity.SysRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-07-21
 */
@Service
public class PytoolAppServiceImpl extends ServiceImpl<PytoolAppMapper, PytoolApp> implements IPytoolAppService {

    @Override
    public IPage<PytoolApp> list(PytoolApp bean, Page page, Map<String, String> map) {
        return baseMapper.list(bean, page, map);
    }

    @Override
    public IPage<PytoolApp> historyList(String softName,Page page) {
        return baseMapper.historyList(softName,page);
    }

    @Override
    public Boolean checkSoftName(String softName) {
        List<PytoolApp> list = this.lambdaQuery().eq(PytoolApp::getSoftName, softName).select(PytoolApp::getId).list();
        return list.size() <= 0;
    }

    @Override
    public Boolean checkSoft(String softName, String softVersion) {
        PytoolApp app = baseMapper.getNewVersionBySoftName(softName);
        if(app == null){
            return false;
        }
        return app.getSoftVersion().equals(softVersion);
    }
}

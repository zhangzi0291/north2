package com.north.pytool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.pytool.entity.PytoolApp;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-07-21
 */
public interface IPytoolAppService extends IService<PytoolApp> {

    IPage<PytoolApp> list(PytoolApp bean, Page page, Map<String, String> map);

    IPage<PytoolApp> historyList(String softName,Page page);

    /**
     * 检查软件名称和版本是否通过，通过true，不通过false
     * @param softName
     * @param softVersion
     * @return
     */
    Boolean checkSoft(String softName,String softVersion);

    /**
     * 检查应用名称是否重复，不重复返回true，重复返回false
     *
     * @param softName
     * @return
     */
    Boolean checkSoftName(String softName);
}

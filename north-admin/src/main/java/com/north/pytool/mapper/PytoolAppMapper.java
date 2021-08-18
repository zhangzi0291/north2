package com.north.pytool.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.pytool.entity.PytoolApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author NorthZX
 * @since 2021-07-21
 */
public interface PytoolAppMapper extends BaseMapper<PytoolApp> {

    IPage<PytoolApp> list(@Param("bean") PytoolApp bean, Page page,@Param("map") Map<String, String> map);

    IPage<PytoolApp> historyList(String softName,Page page);

    PytoolApp getNewVersionBySoftName(String softName);
}

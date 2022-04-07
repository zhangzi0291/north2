package com.north.sys.controller;


import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.sys.entity.SysArea;
import com.north.sys.service.ISysAreaService;
import com.north.utils.ExcelUtil;
import com.north.utils.LambdaUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-04
 */
@RestController
@RequestMapping("/sysArea")
public class SysAreaController extends BaseController<SysArea, ISysAreaService> {

    @Resource
    private ISysAreaService sysAreaService;

    @Override
    protected QueryWrapper<SysArea> setListWrapper(SysArea bean, Map<String, String> map) {
        QueryWrapper<SysArea> qw = super.setListWrapper(bean, map);
        LambdaQueryWrapper<SysArea> qwlambda = qw.lambda();
        qwlambda.orderByAsc(SysArea::getId);
        if (StringUtils.hasLength(bean.getAreaName())) {
            qwlambda.like(SysArea::getAreaName, bean.getAreaName());
        }
        if (bean.getAreaLevel() != null) {
            qwlambda.eq(SysArea::getAreaLevel, bean.getAreaLevel());
        }
        if (bean.getParentId() != null) {
            qwlambda.eq(SysArea::getParentId, bean.getParentId());
        }
        return qw;
    }

    @RequestMapping(path = "export", method = RequestMethod.GET)
    public R export(HttpServletResponse response, SysArea bean, @RequestParam Map<String, String> map) {
        QueryWrapper<SysArea> wrapper = setListWrapper(bean, map);
        List<SysArea> list = sysAreaService.list(wrapper);

        List<ExcelExportEntity> entity = new ArrayList<>();
        entity.add(new ExcelExportEntity("ID", "id"));
        entity.add(new ExcelExportEntity("父区域ID", "parentId"));
        entity.add(new ExcelExportEntity("区域名称", "areaName"));
        entity.add(new ExcelExportEntity("区域级别", "areaLevel"));
        entity.add(new ExcelExportEntity("城乡分类代码", "townCode"));

        List<Map<String, String>> exportList = new ArrayList<Map<String, String>>();
        Map<String, Object> exprotMap;
        for (SysArea object : list) {
            exprotMap = new HashMap<String, Object>();
            exprotMap.put(LambdaUtil.getFieldName(SysArea::getId), object.getId());
            exprotMap.put(LambdaUtil.getFieldName(SysArea::getParentId), object.getParentId());
            exprotMap.put(LambdaUtil.getFieldName(SysArea::getAreaName), object.getAreaName());
            exprotMap.put(LambdaUtil.getFieldName(SysArea::getAreaLevel), object.getAreaLevel());
            exprotMap.put(LambdaUtil.getFieldName(SysArea::getTownCode), object.getTownCode());
            exportList.add(map);
        }
        try {
            String fileName = "区县管理";
            String suffix = ExcelUtil.getSuffix(list.size());
            String downloadFileName = fileName + suffix;
            byte[] bytes = ExcelUtil.export(fileName, fileName, entity, list);

            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8));
            response.getOutputStream().write(bytes);

        } catch (IOException e) {
            logger.error("导出异常", e);
        }
        return null;
    }
}

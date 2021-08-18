package com.north.pytool.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.base.Constant;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.base.exception.curd.InsertFailedException;
import com.north.pytool.entity.PytoolApp;
import com.north.pytool.service.IPytoolAppService;
import com.north.sys.controller.SysFileController;
import com.north.sys.dto.UploadDto;
import com.north.sys.entity.SysUser;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.north.base.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-07-21
 */
@NorthWithoutLogin
@RestController
@RequestMapping("/pytoolApp")
public class PytoolAppController extends BaseController<PytoolApp, IPytoolAppService> {

    @Resource
    private SysFileController sysFileController;

    @Transactional
    @Operation(summary = "添加应用版本", description = "添加软件，并上传软件文件")
        @RequestMapping(path = "addWithFile", method = RequestMethod.POST)
    public R add(HttpServletRequest request, PytoolApp bean, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        //存在版本文件的话先保存版本文件
        if ( !CollectionUtils.isEmpty(files)) {
            try {
                UploadDto dto = sysFileController.saveFile(request, files.get(0));
                bean.setFileId(dto.getFileId());
                bean.setVersionUpdateTime(LocalDateTime.now());
            } catch (IOException e) {
                throw new InsertFailedException("保存图片失败");
            }
        }
        return super.addJson(bean);
    }

    @Transactional
    @Operation(summary = "编辑应用版本", description = "编辑软件，并上传软件文件")
        @RequestMapping(path = "editWithFile", method = RequestMethod.POST)
    public R edit(HttpServletRequest request, PytoolApp bean, @RequestParam(value = "files", required = false) List<MultipartFile> files) throws UnknownHostException {
        PytoolApp old = null;
        try {
            old = this.get(bean.getId()).getData();
        } catch (Exception e) {
            logger.error("查询pytool_app，get失败", e);
            return R.failed("应用版本不存在");
        }

        //存在版本文件的话先保存版本文件
        if ( !CollectionUtils.isEmpty(files)) {
            try {
                UploadDto dto = sysFileController.saveFile(request, files.get(0));
                bean.setFileId(dto.getFileId());
                bean.setVersionUpdateTime(LocalDateTime.now());
            } catch (IOException e) {
                throw new InsertFailedException("保存图片失败");
            }
        }

        if (old.getSoftVersion().equals(bean.getSoftVersion())) {
            //版本相同,编辑同一条数据库记录
            return super.editJson(bean);
        } else {
            //版本不同,新增一条记录
            bean.setId(new Sequence(InetAddress.getLocalHost()).nextId()+"");
            bean.setVersionUpdateTime(LocalDateTime.now());
            return add(request, bean, files);
        }
    }

    @Operation(summary = "查询应用最新版本列表", description = "")
    @RequestMapping(path = "appList", method = RequestMethod.GET)
    public R appList(PytoolApp bean, Page page, @RequestParam Map<String, String> map) {
        QueryWrapper<PytoolApp> wrapper = setListWrapper(bean, map);
        IPage<PytoolApp> list = service.list(bean, page, map);
        return R.ok(list);
    }

    @Operation(summary = "查询指定应用版本历史列表", description = "")
    @RequestMapping(path = "appHistoryList", method = RequestMethod.GET)
    public R appHistoryList(String softName,Page page) {
        IPage<PytoolApp> list = service.historyList(softName,page);
        return R.ok(list);
    }

    /**
     * 检查应用名称重复
     *
     * @param checkValue
     * @param originalValue 旧值，如果有旧值那么旧值和新值可以相同
     * @return
     */
    @Operation(summary = "检查应用名称重复", description = "检查应用名称重复")
    @RequestMapping(path = "checkSoftName", method = RequestMethod.GET)
    public R checkSoftName(String checkValue, String originalValue) {
        if (checkValue.equals(originalValue)) {
            return R.ok();
        }
        if (!service.checkSoftName(checkValue)) {
            return R.failed(ApiErrorCode.CheckFieldError, "应用名称不可重复");
        }
        return R.ok();
    }
}

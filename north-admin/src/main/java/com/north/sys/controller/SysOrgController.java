package com.north.sys.controller;


import com.north.base.BaseController;
import com.north.sys.entity.SysOrg;
import com.north.sys.service.ISysOrgService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 组织 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@RestController
@RequestMapping("/sysOrg")
public class SysOrgController extends BaseController<SysOrg, ISysOrgService> {

}

package com.north.sys.controller;


import com.north.sys.entity.JsonTable;
import com.north.sys.service.IJsonTableService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.north.base.BaseController;

/**
 * <p>
 * json表 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/jsonTable")
public class JsonTableController extends BaseController<JsonTable, IJsonTableService> {

}

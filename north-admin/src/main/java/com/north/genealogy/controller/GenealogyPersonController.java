package com.north.genealogy.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.north.base.BaseController;
import com.north.base.Constant;
import com.north.base.api.R;
import com.north.base.exception.NorthBaseException;
import com.north.genealogy.dto.RelationshipInfo;
import com.north.genealogy.entity.GenealogyPerson;
import com.north.genealogy.entity.GenealogyPersonTimeline;
import com.north.genealogy.service.IGenealogPersonTimelineService;
import com.north.genealogy.service.IGenealogyPersonService;
import com.north.sys.entity.SysFile;
import com.north.sys.service.ISysFileService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 族谱中的人信息 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/genealogyPerson")
public class GenealogyPersonController extends BaseController<GenealogyPerson, IGenealogyPersonService> {

    @Resource
    private IGenealogyPersonService personService;
    @Resource
    private ISysFileService sysFileService;
    @Resource
    private IGenealogPersonTimelineService timelineService;

    @RequestMapping("addGenealogyPerson")
    public R addGenealogyPerson(@RequestParam(value = "file", required = false) List<MultipartFile> files, RelationshipInfo relationshipInfo, GenealogyPerson person, @RequestParam(value = "personTimelines", required = false) String personTimelineJsonStr) {
        List<GenealogyPersonTimeline> genealogyPersonTimelineList = null;
        if (StringUtils.hasLength(personTimelineJsonStr)) {
            try {
                genealogyPersonTimelineList = JSONArray.parseArray(personTimelineJsonStr, GenealogyPersonTimeline.class);
            } catch (Exception e) {
                throw new NorthBaseException("时间线参数错误");
            }
        }
        personService.addGenealogyPerson(files, relationshipInfo, person, genealogyPersonTimelineList);
        return R.ok();
    }

    @RequestMapping("getGenealogyPersonImages")
    public R getGenealogyPersonImages(String id) {
        QueryWrapper<SysFile> qw = new QueryWrapper();
        qw.lambda()
                .eq(SysFile::getModuleName, Constant.GENEALOGY_MODULE_NAME)
                .eq(SysFile::getRelationId, id);
        List<SysFile> list = sysFileService.list(qw);
        List<String> imageIds = new ArrayList<>();
        for (SysFile sysFile : list) {
            imageIds.add(sysFile.getId());
        }
        return R.ok(imageIds);
    }

    @RequestMapping("getGenealogyPersonTimeLines")
    public R getGenealogyPersonTimeLines(String id) {
        QueryWrapper<GenealogyPersonTimeline> qw = new QueryWrapper();
        qw.lambda()
                .eq(GenealogyPersonTimeline::getPersonId, id)
                .orderByDesc(GenealogyPersonTimeline::getEventTime);
        List<GenealogyPersonTimeline> list = timelineService.list(qw);
        return R.ok(list);
    }

    @RequestMapping("editGenealogyPerson")
    public R editGenealogyPerson(@RequestParam(value = "file", required = false) List<MultipartFile> files, @RequestParam(value = "fileId", required = false) List<String> fileIds, RelationshipInfo relationshipInfo, GenealogyPerson person, @RequestParam(value = "personTimelines", required = false) String personTimelineJsonStr) {
        List<GenealogyPersonTimeline> genealogyPersonTimelineList = null;
        if (StringUtils.hasLength(personTimelineJsonStr)) {
            try {
                genealogyPersonTimelineList = JSONArray.parseArray(personTimelineJsonStr, GenealogyPersonTimeline.class);
            } catch (Exception e) {
                throw new NorthBaseException("时间线参数错误");
            }
        }
        personService.editGenealogyPerson(files, fileIds, relationshipInfo, person, genealogyPersonTimelineList);
        return R.ok();
    }

    @RequestMapping("removeGenealogyPerson")
    public R removeGenealogyPerson(String id) {
        service.removeGenealogyPerson(id);
        return R.ok();
    }
}

package com.north.genealogy.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.base.exception.impl.CurlExceptionEnum;
import com.north.genealogy.dto.GenealogyPersonDto;
import com.north.genealogy.dto.Link;
import com.north.genealogy.entity.Genealogy;
import com.north.genealogy.entity.GenealogyParentTree;
import com.north.genealogy.entity.GenealogyPartnerTree;
import com.north.genealogy.entity.GenealogyPerson;
import com.north.genealogy.service.IGenealogyParentTreeService;
import com.north.genealogy.service.IGenealogyPartnerTreeService;
import com.north.genealogy.service.IGenealogyPersonService;
import com.north.genealogy.service.IGenealogyService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 族谱 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/genealogy")
public class GenealogyController extends BaseController<Genealogy, IGenealogyService> {

    @Resource
    private IGenealogyParentTreeService parentTreeService;
    @Resource
    private IGenealogyPartnerTreeService partnerTreeService;
    @Resource
    private IGenealogyPersonService personService;

    @NorthWithoutLogin
    @RequestMapping("getGenealogyTree")
    @ValidateParams(
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "genealogyId")
    )
    public R getGenealogyTree(String genealogyId) {
        QueryWrapper<GenealogyParentTree> qw1 = new QueryWrapper();
        qw1.lambda().eq(GenealogyParentTree::getGenealogyId, genealogyId);
        List<GenealogyParentTree> parentTreeList = parentTreeService.list(qw1);
        QueryWrapper<GenealogyPartnerTree> qw2 = new QueryWrapper();
        qw2.lambda().eq(GenealogyPartnerTree::getGenealogyId, genealogyId);
        List<GenealogyPartnerTree> partnerTreeList = partnerTreeService.list(qw2);

        Set<String> personIdSet = new HashSet<>();
        List<Link> links = new ArrayList<>();
        for (GenealogyParentTree treeNode : parentTreeList) {
            Link link = new Link(treeNode.getParentId(), treeNode.getPersonId(), "子女");
            links.add(link);
            personIdSet.add(treeNode.getParentId());
            personIdSet.add(treeNode.getPersonId());
        }
        for (GenealogyPartnerTree treeNode : partnerTreeList) {
            Link link = new Link(treeNode.getPartnerId(), treeNode.getPersonId(), "配偶");
            links.add(link);
            personIdSet.add(treeNode.getPersonId());
            personIdSet.add(treeNode.getPartnerId());
        }

        if (personIdSet.isEmpty()) {
            CurlExceptionEnum.GENERAL_FAILED.assertTrue(false,"族谱一个人也没有");
        }
        List<GenealogyPerson> persons = personService.listByIds(personIdSet);

        List<GenealogyPersonDto> nodes = new ArrayList<>();
        for (GenealogyPerson person : persons) {
            GenealogyPersonDto node = new GenealogyPersonDto();
            BeanUtil.copyProperties(person, node);
            List<Link> linkList = links.stream().filter(link -> person.getId().equals(link.getTarget())).collect(Collectors.toList());
            for (Link link : linkList) {
                if ("配偶".equals(link.getRelationship())) {
                    node.setPartnerId(link.getSource());
                }
                if ("子女".equals(link.getRelationship())) {
                    node.setParentId(link.getSource());
                }
            }
            nodes.add(node);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("links", links);
        resultMap.put("nodes", nodes);
        return R.ok(resultMap);
    }

    @RequestMapping("getGenealogyTreePersonSearch")
    @ValidateParams(
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "genealogyId")
    )
    public R getGenealogyTreePersonSearch(String genealogyId, String personName) {
        QueryWrapper<GenealogyParentTree> qw1 = new QueryWrapper();
        qw1.lambda().eq(GenealogyParentTree::getGenealogyId, genealogyId);
        List<GenealogyParentTree> parentTreeList = parentTreeService.list(qw1);
        Set<String> personIdSet = new HashSet<>();
        for (GenealogyParentTree treeNode : parentTreeList) {
            personIdSet.add(treeNode.getParentId());
            personIdSet.add(treeNode.getPersonId());
        }
        List<GenealogyPerson> persons = personService.listByIds(personIdSet);
        if (StringUtils.hasLength(personName)) {
            persons = persons.stream().filter(person -> person.getPersonName().startsWith(personName)).collect(Collectors.toList());
        }
        return R.ok(persons);
    }
}

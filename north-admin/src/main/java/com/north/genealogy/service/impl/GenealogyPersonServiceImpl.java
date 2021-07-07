package com.north.genealogy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.base.Constant;
import com.north.base.api.R;
import com.north.genealogy.dto.RelationshipInfo;
import com.north.genealogy.entity.GenealogyParentTree;
import com.north.genealogy.entity.GenealogyPartnerTree;
import com.north.genealogy.entity.GenealogyPerson;
import com.north.genealogy.entity.GenealogyPersonTimeline;
import com.north.genealogy.mapper.GenealogyPersonMapper;
import com.north.genealogy.service.IGenealogPersonTimelineService;
import com.north.genealogy.service.IGenealogyParentTreeService;
import com.north.genealogy.service.IGenealogyPartnerTreeService;
import com.north.genealogy.service.IGenealogyPersonService;
import com.north.sys.controller.SysFileController;
import com.north.sys.dto.UploadDto;
import com.north.sys.entity.SysFile;
import com.north.sys.service.ISysFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 族谱中的人信息 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@Service
public class GenealogyPersonServiceImpl extends ServiceImpl<GenealogyPersonMapper, GenealogyPerson> implements IGenealogyPersonService {

    @Resource
    private SysFileController sysFileController;
    @Resource
    private IGenealogPersonTimelineService genealogPersonTimelineService;
    @Resource
    private IGenealogyParentTreeService genealogyParentTreeService;
    @Resource
    private IGenealogyPartnerTreeService genealogyPartnerTreeService;
    @Resource
    private ISysFileService sysFileService;

    @Override
    public void addGenealogyPerson(List<MultipartFile> images, RelationshipInfo relationshipInfo, GenealogyPerson person, List<GenealogyPersonTimeline> timelineList) {
        this.save(person);
        //如果有图片保存图片
        saveImages(images, person.getId());
        //保存关系
        saveRelationshipInfo(relationshipInfo, person);
        //如果有时间线保存时间线
        saveGenealogPersonTimeline(person, timelineList);
    }

    @Override
    public void editGenealogyPerson(List<MultipartFile> images, List<String> fileId, RelationshipInfo relationshipInfo, GenealogyPerson person, List<GenealogyPersonTimeline> timelineList) {
        this.updateById(person);
        //删除不存在的的照片,保存新照片
        removeImages(fileId, person.getId());
        saveImages(images, person.getId());
        //删除之前的关系，关系分子女和配偶需要删除两个表，保存新关系
        removeRelationShipInfo(person.getId());
        saveRelationshipInfo(relationshipInfo, person);
        //删除之前的时间线,保存新的
        removePersonTimeline(person.getId());
        saveGenealogPersonTimeline(person, timelineList);

    }

    @Override
    public void removeGenealogyPerson(String personId) {
        this.removeById(personId);
        removeImages(new ArrayList<>(), personId);
        removePersonTimeline(personId);
        removePersonTimeline(personId);
    }

    /**
     * 保存照片/
     *
     * @param images
     * @param personId
     */
    private void saveImages(List<MultipartFile> images, String personId) {
        if (!CollectionUtils.isEmpty(images)) {
            for (MultipartFile image : images) {
                try {
                    saveFile(image, personId);
                } catch (IOException e) {
                    log.error("保存图片失败：", e);
                }
            }
        }
    }

    /**
     * 保存时间线
     *
     * @param person
     * @param timelineList
     */
    public void saveGenealogPersonTimeline(GenealogyPerson person, List<GenealogyPersonTimeline> timelineList) {
        if (!CollectionUtils.isEmpty(timelineList)) {
            for (GenealogyPersonTimeline timeline : timelineList) {
                timeline.setPersonId(person.getId());
            }
            genealogPersonTimelineService.saveBatch(timelineList);
        }
    }

    /**
     * 保存人物关系
     *
     * @param relationshipInfo
     * @param person
     */
    private void saveRelationshipInfo(RelationshipInfo relationshipInfo, GenealogyPerson person) {
        if (StringUtils.hasLength(relationshipInfo.getParentId())) {
            //配置是谁子女最优先生效
            GenealogyParentTree parentTree = new GenealogyParentTree();
            parentTree.setGenealogyId(relationshipInfo.getGenealogyId());
            parentTree.setPersonId(person.getId());
            parentTree.setParentId(relationshipInfo.getParentId());
            genealogyParentTreeService.save(parentTree);
        } else if (StringUtils.hasLength(relationshipInfo.getPartnerId())) {
            //配置是谁配偶
            GenealogyPartnerTree partnerTree = new GenealogyPartnerTree();
            partnerTree.setGenealogyId(relationshipInfo.getGenealogyId());
            partnerTree.setPersonId(person.getId());
            partnerTree.setPartnerId(relationshipInfo.getPartnerId());
            genealogyPartnerTreeService.save(partnerTree);
        } else {
            //没有配置是谁子女或配偶
            GenealogyParentTree parentTree = new GenealogyParentTree();
            parentTree.setGenealogyId(relationshipInfo.getGenealogyId());
            parentTree.setPersonId(person.getId());
            genealogyParentTreeService.save(parentTree);
        }
    }

    /**
     * 保存图片
     *
     * @param file
     * @param personId
     * @return
     * @throws IOException
     */
    private UploadDto saveFile(MultipartFile file, String personId) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        R<UploadDto> r = sysFileController.uploadWithMd5Check(request, file, "genealogy", personId, false);
        return r.getData();
    }

    /**
     * 删除照片
     *
     * @param fileId
     * @param personId
     */
    private void removeImages(List<String> fileId, String personId) {
        QueryWrapper<SysFile> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(SysFile::getRelationId, personId)
                .eq(SysFile::getModuleName, Constant.GENEALOGY_MODULE_NAME)
        ;
        if (!CollectionUtils.isEmpty(fileId)) {
            qw.lambda().notIn(SysFile::getId, fileId);
        }
        sysFileService.remove(qw);
    }

    /**
     * 删除时间线
     *
     * @param personId
     */
    private void removePersonTimeline(String personId) {
        QueryWrapper<GenealogyPersonTimeline> qw = new QueryWrapper<>();
        qw.lambda().eq(GenealogyPersonTimeline::getPersonId, personId);
        genealogPersonTimelineService.remove(qw);
    }

    /**
     * 删除人物关系
     *
     * @param personId
     */
    private void removeRelationShipInfo(String personId) {
        QueryWrapper<GenealogyParentTree> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(GenealogyParentTree::getPersonId, personId);
        genealogyParentTreeService.remove(qw1);
        QueryWrapper<GenealogyPartnerTree> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(GenealogyPartnerTree::getPersonId, personId);
        genealogyPartnerTreeService.remove(qw2);
    }


}

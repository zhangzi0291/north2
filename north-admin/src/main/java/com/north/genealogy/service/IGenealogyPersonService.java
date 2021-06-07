package com.north.genealogy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.genealogy.dto.RelationshipInfo;
import com.north.genealogy.entity.GenealogyPerson;
import com.north.genealogy.entity.GenealogyPersonTimeline;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 族谱中的人信息 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
public interface IGenealogyPersonService extends IService<GenealogyPerson> {

    /**
     * 添加人员
     *
     * @param images
     * @param relationshipInfo
     * @param person
     * @param timelineList
     */
    @Transactional
    void addGenealogyPerson(List<MultipartFile> images, RelationshipInfo relationshipInfo, GenealogyPerson person, List<GenealogyPersonTimeline> timelineList);

    /**
     * 编辑人员
     *
     * @param images
     * @param fileId           文件没有变化的ID集合
     * @param relationshipInfo
     * @param person
     * @param timelineList
     */
    @Transactional
    void editGenealogyPerson(List<MultipartFile> images, List<String> fileId, RelationshipInfo relationshipInfo, GenealogyPerson person, List<GenealogyPersonTimeline> timelineList);

    /**
     * 删除人员
     *
     * @param personId
     */
    @Transactional
    void removeGenealogyPerson(String personId);
}

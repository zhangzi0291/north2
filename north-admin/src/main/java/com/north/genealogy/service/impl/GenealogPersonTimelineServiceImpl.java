package com.north.genealogy.service.impl;

import com.north.genealogy.entity.GenealogyPersonTimeline;
import com.north.genealogy.mapper.GenealogPersonTimelineMapper;
import com.north.genealogy.service.IGenealogPersonTimelineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 族谱中的人时间轴 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-20
 */
@Service
public class GenealogPersonTimelineServiceImpl extends ServiceImpl<GenealogPersonTimelineMapper, GenealogyPersonTimeline> implements IGenealogPersonTimelineService {

}

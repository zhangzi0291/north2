package com.north.genealogy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.genealogy.entity.GenealogyParentTree;
import com.north.genealogy.mapper.GenealogyParentTreeMapper;
import com.north.genealogy.service.IGenealogyParentTreeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 族谱-父子树 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@Service
public class GenealogyParentTreeServiceImpl extends ServiceImpl<GenealogyParentTreeMapper, GenealogyParentTree> implements IGenealogyParentTreeService {

}

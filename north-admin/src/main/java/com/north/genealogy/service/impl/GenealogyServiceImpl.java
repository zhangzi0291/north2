package com.north.genealogy.service.impl;

import com.north.genealogy.entity.Genealogy;
import com.north.genealogy.mapper.GenealogyMapper;
import com.north.genealogy.service.IGenealogyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 族谱 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@Service
public class GenealogyServiceImpl extends ServiceImpl<GenealogyMapper, Genealogy> implements IGenealogyService {

}

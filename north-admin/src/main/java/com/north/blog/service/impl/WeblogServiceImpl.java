package com.north.blog.service.impl;

import com.north.blog.entity.Weblog;
import com.north.blog.mapper.WeblogMapper;
import com.north.blog.service.IWeblogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网络日志 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2022-01-21
 */
@Service
public class WeblogServiceImpl extends ServiceImpl<WeblogMapper, Weblog> implements IWeblogService {

}

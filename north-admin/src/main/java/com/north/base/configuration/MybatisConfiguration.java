package com.north.base.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018-9-1 15:55
 */
@Configuration
@MapperScan("com.north.*.mapper")
public class MybatisConfiguration implements MetaObjectHandler {

    private static final String CREATED_BY = "createdBy";
    private static final String CREATED_TIME = "createdTime";
    private static final String UPDATED_BY = "updatedBy";
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime nowTime = LocalDateTime.now();
        this.setFieldValByName(CREATED_BY, "fillTest", metaObject);
        this.setFieldValByName(CREATED_TIME, nowTime, metaObject);
        this.setFieldValByName(UPDATED_BY, "fillTest", metaObject);
        this.setFieldValByName(UPDATE_TIME, nowTime, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime nowTime = LocalDateTime.now();
        this.setFieldValByName(UPDATED_BY, "fillTest", metaObject);
        this.setFieldValByName(UPDATE_TIME, nowTime, metaObject);
    }

}

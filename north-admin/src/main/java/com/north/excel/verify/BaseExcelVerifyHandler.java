package com.north.excel.verify;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.north.base.cache.BaseRedisCacheService;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-30
 */
public abstract class BaseExcelVerifyHandler<T> implements IExcelVerifyHandler<Map<String, Object>> {

    protected BaseRedisCacheService<T> baseRedisCacheService;

    BaseExcelVerifyHandler(BaseRedisCacheService<T> baseRedisCacheService) {
        this.baseRedisCacheService = baseRedisCacheService;
    }

    /**
     * 需要实现转换读取的map变成bean
     * 因为map的key是excel的第一行表头所以需要手动转换
     *
     * @param obj
     * @return
     */
    public abstract T setBean(Map<String, Object> obj);

    /**
     * 需要实现额外的校验
     *
     * @param obj
     * @return 校验失败返回错误的描述，校验成功返回null
     */
    public abstract String verify(Map<String, Object> obj);

    @Override
    public ExcelVerifyHandlerResult verifyHandler(Map<String, Object> obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();
        result.setSuccess(true);
        T bean = setBean(obj);
        if (baseRedisCacheService.existData(bean)) {
            result.setSuccess(false);
            result.setMsg("记录重复");
            return result;
        }

        String message = verify(obj);
        if (StringUtils.hasLength(message)) {
            result.setSuccess(false);
            result.setMsg(message);
            return result;
        }

        baseRedisCacheService.addData(bean);
        return result;
    }

}

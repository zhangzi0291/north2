package com.north.utils;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-26
 */
public class LambdaUtil {

    public static <T> String getFieldName(SFunction<T, ?> function) {
        return PropertyNamer.methodToProperty(LambdaUtils.resolve(function).getImplMethodName());
    }

}

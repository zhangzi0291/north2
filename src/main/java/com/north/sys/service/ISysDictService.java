package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysDict;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-22
 */
public interface ISysDictService extends IService<SysDict> {

    default Object getDictValue(SysDict dict) {
        String value = dict.getDictValue();
        if (dict.getValueType() == null || dict.getValueType().equals(1)) {
            //值为字符串
            return value;
        } else if (dict.getValueType().equals(2)) {
            //值为数字
            return Double.valueOf(value);
        } else {
            return value;
        }
    }

    Boolean checkDict(String dictName, String dictKey);
}

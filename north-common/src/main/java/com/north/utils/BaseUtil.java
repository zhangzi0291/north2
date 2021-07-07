package com.north.utils;

import com.north.sys.dto.TreeDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-29
 */
public class BaseUtil {

    /**
     * 获取树形结构<Br>
     * 需要先构造TreeDto，并通过TreeDto的pid和id来区分层级，并指定顶级pid
     *
     * @param list
     * @param pid  顶级pid
     * @param <T>
     * @return
     */
    public static <T> List<TreeDto<T>> getTreeDtoList(List<TreeDto<T>> list, String pid) {
        List<TreeDto<T>> resultList = new CopyOnWriteArrayList<>();

        Map<String, List<TreeDto<T>>> map = list.stream().collect(Collectors.groupingBy(res -> res.getPid()));

        for (TreeDto<T> p : list) {
            map.forEach((String key, List<TreeDto<T>> value) -> {
                if (p.getId().equals(key)) {
                    if (p.getChild() == null) {
                        p.setChild(new ArrayList<>());
                    }
                    p.getChild().addAll((Collection<? extends T>) value);
                }
            });
            resultList.add(p);
        }
        for (TreeDto<T> treeDto : resultList) {
            if (!treeDto.getPid().equals(pid)) {
                resultList.remove(treeDto);
            }
        }
        return resultList;
    }

}

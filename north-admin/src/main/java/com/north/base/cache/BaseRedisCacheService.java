package com.north.base.cache;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 导入时缓存比较数据
 *
 * @author Northzx
 * @version 1.0
 * @since 2021-03-30
 */
public abstract class BaseRedisCacheService<T> {

    /**
     * redis的KEY前缀
     */
    protected String prefix = "north:cache:";

    /**
     * redis缓存保存时间
     */
    protected int timeout = 60;

    public BaseRedisCacheService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.initData((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected RedissonClient redissonClient;

    /**
     * 获取缓存用的key
     *
     * @param clazz
     * @return
     */
    private String getRedisKeyName(Class clazz) {
        return prefix + clazz.getSimpleName();
    }

    /**
     * 获取本次导入比较用的key
     *
     * @param clazz
     * @return
     */
    private String getTmpRedisKeyName(Class clazz) {
        return prefix + "tmp" + clazz.getSimpleName();
    }

    /**
     * 更新缓存的数
     */
    public void updateAllData() {
        List<T> listBean = getAllList();
        RList<T> list = redissonClient.getList(getRedisKeyName(listBean.get(0).getClass()));
        if (list.isExists()) {
            list.clear();
        }
        list.addAll(listBean);
        list.expire(timeout, TimeUnit.MINUTES);
    }

    /**
     * 添加缓存的数据
     *
     * @param bean
     */
    public void addData(T bean) {
        RList<T> list = redissonClient.getList(getRedisKeyName(bean.getClass()));
        list.add(bean);
    }

    /**
     * 判断数据是否存在缓存中
     *
     * @param bean
     * @return
     */
    public boolean existData(T bean) {
        RList<T> list = redissonClient.getList(getRedisKeyName(bean.getClass()));
        if (!list.isExists()) {
            updateAllData();
        }
        for (T listBean : list) {
            if (checkBean(bean, listBean)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化缓存
     *
     * @param clazz
     */
    private void initData(Class<T> clazz) {
        RList<T> list = redissonClient.getList(getRedisKeyName(clazz));
        list.clear();
    }

    /**
     * 需要实现获取待比较的缓存数据
     *
     * @return
     */
    protected abstract List<T> getAllList();

    /**
     * 需要实现比较两个对象是否相同，相同返回true
     *
     * @param bean
     * @param listBean
     * @return
     */
    public abstract Boolean checkBean(T bean, T listBean);

}

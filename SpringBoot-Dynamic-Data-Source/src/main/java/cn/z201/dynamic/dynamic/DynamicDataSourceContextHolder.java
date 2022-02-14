package cn.z201.dynamic.dynamic;

import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
public class DynamicDataSourceContextHolder {

    private static class SingletonHolder {
        private static final DynamicDataSourceContextHolder INSTANCE = new DynamicDataSourceContextHolder();
    }

    private DynamicDataSourceContextHolder() {
    }

    public static final DynamicDataSourceContextHolder getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>() {
        /**
         * 将 master 数据源的 key作为默认数据源的 key
         */
        @Override
        protected String initialValue() {
            return DynamicDataSourceConstant.MASTER;
        }
    };
    /**
     * 数据源的 key集合，用于切换时判断数据源是否存在
     */
    private static Set<Object> dataSourceKeys = Collections.synchronizedSet(new HashSet<>());

    /**
     * 切换数据源
     *
     * @param key 数据源
     */
    public void setDataSourceKey(String key) {
        if (containDataSourceKey(key)) {
            if (!ObjectUtils.isEmpty(key)) {
                contextHolder.set(key);
            }
        }
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public String getDataSourceKey() {
        return contextHolder.get();
    }

    /**
     * 重置数据源
     */
    public void clearDataSourceKey() {
        contextHolder.remove();
    }

    /**
     * 判断是否包含数据源
     *
     * @param key 数据源
     * @return
     */
    public boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }

    /**
     * 添加数据源Keys
     *
     * @param keys
     * @return
     */
    public boolean addDataSourceKeys(Collection<? extends Object> keys) {
        return dataSourceKeys.addAll(keys);
    }

    /**
     * 获取全部数据源列表
     * @return
     */
    public Set<Object> all(){
        return dataSourceKeys;
    }
}
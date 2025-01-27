package cn.z201.redis;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
public interface DelayOrderI<T> {

    /**
     * 添加延迟对象到延时队列
     * @param itemDelayed 延迟对象
     * @return boolean
     */
    boolean addToOrderDelayQueue(ItemDelayedI<T> itemDelayed);

    /**
     * 根据对象添加到指定延时队列
     * @param data 数据对象
     * @return boolean
     */
    boolean addToDelayQueue(T data);

    /**
     * 移除指定的延迟对象从延时队列中
     * @param data
     */
    void removeToOrderDelayQueue(T data);

    /**
     * 获取列队总所有数据
     * @return
     */
    List<T> all();

    /**
     * 长度
     * @return
     */
    Long size();

}

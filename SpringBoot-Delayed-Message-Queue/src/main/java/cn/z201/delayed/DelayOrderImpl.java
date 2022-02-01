package cn.z201.delayed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Lazy(false)
@Component
public class DelayOrderImpl implements DelayOrderI<OrderBo> {

    @Autowired
    @Qualifier("defExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final static DelayQueue<ItemDelayedI<OrderBo>> DELAY_QUEUE = new DelayQueue<>();

    /**
     * 初始化时加载数据库中需处理超时的订单
     */
    @PostConstruct
    public void init() {
        /*启动一个线程，去取延迟订单*/
        threadPoolTaskExecutor.execute(() -> {
            ItemDelayedI<OrderBo> orderDelayed;
            for (; ; ) {
                try {
                    orderDelayed = DELAY_QUEUE.take();
                    //处理超时订单
                    log.info("处理超时订单 {} {}", orderDelayed.getDataId(), DateTool.conversionNowFormat());
                } catch (Exception e) {
                    log.error("执行自营超时订单的_延迟队列_异常:" + e);
                }
            }
        });
    }

    /**
     * 加入延迟消息队列
     **/
    @Override
    public boolean addToOrderDelayQueue(ItemDelayedI<OrderBo> itemDelayed) {
        return DELAY_QUEUE.add(itemDelayed);
    }

    /**
     * 加入延迟消息队列
     **/
    @Override
    public boolean addToDelayQueue(OrderBo order) {
        ItemDelayedI<OrderBo> orderDelayed = new ItemDelayedI<>(order.getId(), order.getCreateTime(), order.getOrderDeadlineTime());
        if (DELAY_QUEUE.contains(orderDelayed)) {
            return true;
        }
        log.info("添加订单超时列队 {} {}", JsonTool.toString(order), DateTool.conversionNowFormat());
        return DELAY_QUEUE.add(orderDelayed);
    }

    /**
     * 移除列队
     *
     * @param order
     */
    @Override
    public void removeToOrderDelayQueue(OrderBo order) {
        if (order == null) {
            return;
        }
        for (Iterator<ItemDelayedI<OrderBo>> iterator = DELAY_QUEUE.iterator(); iterator.hasNext(); ) {
            ItemDelayedI<OrderBo> queue = iterator.next();
            if (queue.getDataId().equals(order.getId())) {
                log.info("移除订单超时列队 {} {}", order, DateTool.conversionNowFormat());
                DELAY_QUEUE.remove(queue);
            }
        }
    }

    public void removeToOrderDelayQueueById(Long id) {
        if (id == null) {
            return;
        }
        log.info("移除订单超时列队 {} {}", id, DateTool.conversionNowFormat());
        for (Iterator<ItemDelayedI<OrderBo>> iterator = DELAY_QUEUE.iterator(); iterator.hasNext(); ) {
            ItemDelayedI<OrderBo> queue = iterator.next();
            if (queue.getDataId().equals(id)) {
                DELAY_QUEUE.remove(queue);
            }
        }
    }

    @Override
    public List<OrderBo> all() {
        List<OrderBo> list = new ArrayList<>();
        OrderBo orderBoTemp = null;
        for (Iterator<ItemDelayedI<OrderBo>> iterator = DELAY_QUEUE.iterator(); iterator.hasNext(); ) {
            ItemDelayedI<OrderBo> queue = iterator.next();
            orderBoTemp = OrderBo.builder()
                    .id(queue.getDataId())
                    .orderDeadlineTime(queue.getExpire())
                    .createTime(queue.getStartTime())
                    .build();
            list.add(orderBoTemp);
        }
        return list;
    }
}

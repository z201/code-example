package cn.z201.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Lazy(false)
@Component
public class DelayOrderImpl implements DelayOrderI<OrderBo> {

    private final static String ORDER_DELAY_KEY = "order:delay";

    public final static int ORDER_DELAY_TIME = 1;

    @Autowired
    @Qualifier("defExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 初始化时加载数据库中需处理超时的订单
     */
    @PostConstruct
    public void init() {
        /*启动一个线程，去取延迟订单*/
        threadPoolTaskExecutor.execute(() -> {
            log.info(" 待处理订单数量 count {} " , size());
            for (; ; ) {
                try {
                    Long startTime = DateTool.conversion(DateTool.localDateTime().plusDays(-1));
                    // 测试下直接获取所有数据。
                    Set<Integer> data = redisTemplate.opsForZSet().rangeByScore(ORDER_DELAY_KEY,0,DateTool.currentTimeMillis());
                    data.stream().forEach(i->{
                        //处理超时订单
                        log.info("处理订单 {} {}", i, DateTool.conversionNowFormat());
                        removeToOrderDelayQueueById(Long.valueOf(i));
                    });
                } catch (Exception e) {
                    log.error("执行订单的_延迟队列_异常:" + e);
                }finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("执行订单的_延迟队列_异常:" + e);
                    }
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        threadPoolTaskExecutor.shutdown();
    }

    /**
     * 加入延迟消息队列
     **/
    @Override
    public boolean addToOrderDelayQueue(ItemDelayedI<OrderBo> itemDelayed) {
        log.info("添加订单超时列队 {} {} 移除时间 {}", JsonTool.toString(itemDelayed), DateTool.conversionNowFormat(),DateTool.conversionFormat(itemDelayed.getExpire()));
        return redisTemplate.opsForZSet().add(ORDER_DELAY_KEY, itemDelayed.getDataId(), itemDelayed.getExpire());
    }

    /**
     * 加入延迟消息队列
     **/
    @Override
    public boolean addToDelayQueue(OrderBo order) {
        ItemDelayedI<OrderBo> orderDelayed = new ItemDelayedI<>(order.getId(), order.getCreateTime(), order.getOrderDeadlineTime());
        return addToOrderDelayQueue(orderDelayed);
    }

    /**
     * 移除列队
     *
     * @param order
     */
    @Override
    public void removeToOrderDelayQueue(OrderBo order) {
        if (null == order || null == order.getId()) {
            return;
        }
        removeToOrderDelayQueueById(order.getId());
    }

    public void removeToOrderDelayQueueById(Long id) {
        if (id == null) {
            return;
        }
        log.info("移除订单超时列队 {} {}", id, DateTool.conversionNowFormat());
        redisTemplate.opsForZSet().remove(ORDER_DELAY_KEY, id);
    }

    @Override
    public List<OrderBo> all() {
        List<OrderBo> list = new ArrayList<>();
        Map<String, Long> orderZSetMap = zScan(ORDER_DELAY_KEY, "*", 100);
        orderZSetMap.forEach((key, value) -> {
            list.add(OrderBo.builder()
                    .id(Long.valueOf(key))
                    .orderDeadlineTime(value)
                    .createTime(DateTool.conversion(DateTool.conversion(value).plusMinutes(-ORDER_DELAY_TIME)))
                    .build());
        });
        return list;
    }

    @Override
    public Long size() {
        return redisTemplate.opsForZSet().size(ORDER_DELAY_KEY);
    }

    /**
     * {@link = http://redisdoc.com/database/scan.html#zscan}
     * <p>
     * 可用版本： >= 2.8.0
     * 时间复杂度：增量式迭代命令每次执行的复杂度为 O(1) ， 对数据集进行一次完整迭代的复杂度为 O(N) ， 其中 N 为数据集中的元素数量。
     *
     * @param key
     * @param pattern
     * @param count
     * @return
     */
    private Map<String, Long> zScan(String key, String pattern, Integer count) {
        Map<String, Long> map = new HashMap<>();
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(count)
                .build();
        Cursor<RedisZSetCommands.Tuple> cursor = connection.zScan(stringRedisSerializer.serialize(key), scanOptions);
        while (cursor.hasNext()) {
            RedisZSetCommands.Tuple data = cursor.next();
            if (null != data) {
                map.put(stringRedisSerializer.deserialize(data.getValue()), data.getScore().longValue());
            }
        }
        return map;
    }
}

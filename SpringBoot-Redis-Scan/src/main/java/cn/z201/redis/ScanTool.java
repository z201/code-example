package cn.z201.redis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Component
public class ScanTool {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * SCAN 命令是一个基于游标的迭代器（cursor based iterator）： SCAN 命令每次被调用之后， 都会向用户返回一个新的游标，
     * 用户在下次迭代时需要使用这个新游标作为 SCAN 命令的游标参数， 以此来延续之前的迭代过程。
     * 当 SCAN 命令的游标参数被设置为 0 时， 服务器将开始一次新的迭代， 而当服务器向用户返回值为 0 的游标时， 表示迭代已结束。
     *
     * @param pattern
     * @param count
     * @return
     */
    public Set<String> scan(String pattern, Integer count) {
        Set<String> keys = new HashSet<>();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(count)
                .build();
        Cursor<byte[]> cursor = connection.scan(scanOptions);
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }
        return keys;
    }

    /**
     * {@link = http://redisdoc.com/database/scan.html#hscan}
     * <p>
     * 可用版本： >= 2.8.0
     * 时间复杂度：增量式迭代命令每次执行的复杂度为 O(1) ， 对数据集进行一次完整迭代的复杂度为 O(N) ， 其中 N 为数据集中的元素数量。
     *
     * @param key
     * @param pattern
     * @param count
     * @return
     */
    public Map<String,String> hScan(String key,String pattern, Integer count) {
        Map<String,String> map = new HashMap<>();
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(count)
                .build();
        Cursor<Map.Entry<byte[], byte[]>> cursor = connection.hScan(stringRedisSerializer.serialize(key),scanOptions);
        while (cursor.hasNext()) {
            Map.Entry<byte[], byte[]> data = cursor.next();
            if (null != data) {
                map.put(stringRedisSerializer.deserialize(data.getKey()),stringRedisSerializer.deserialize(data.getValue()));
            }
        }
        return map;
    }

    /**
     * {@link = http://redisdoc.com/database/scan.html#sscan}
     * <p>
     * 可用版本： >= 2.8.0
     * 时间复杂度：增量式迭代命令每次执行的复杂度为 O(1) ， 对数据集进行一次完整迭代的复杂度为 O(N) ， 其中 N 为数据集中的元素数量。
     *
     * @param key
     * @param pattern
     * @param count
     * @return
     */
    public Set<String> sScan(String key, String pattern, Integer count) {
        Set<String> set = new HashSet<>();
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(count)
                .build();
        Cursor<byte[]> cursor = connection.sScan(stringRedisSerializer.serialize(key),scanOptions);
        while (cursor.hasNext()) {
            byte[] data = cursor.next();
            if (null != data) {
                set.add(stringRedisSerializer.deserialize(data));
            }
        }
        return set;
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
    public Map<String,Long> zScan(String key, String pattern, Integer count) {
        Map<String,Long> map = new HashMap<>();
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(count)
                .build();
        Cursor<RedisZSetCommands.Tuple> cursor = connection.zScan(stringRedisSerializer.serialize(key),scanOptions);
        while (cursor.hasNext()) {
            RedisZSetCommands.Tuple data = cursor.next();
            if (null != data) {
                map.put(stringRedisSerializer.deserialize(data.getValue()),data.getScore().longValue());
            }
        }
        return map;
    }

}

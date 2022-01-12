package cn.z201.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class DistributedLockRedisTool {

    private static String LOCK_PREFIX = "lock:";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 尝试获取分布式锁,设置重试次数
     *
     * @param key
     * @param value
     * @param retries
     * @param timeout
     * @return
     */
    public boolean lock(String key, String value, int retries, long timeout) {
        Boolean result = Boolean.FALSE;
        // 最多重试三次
        if (retries > 3) {
            retries = 3;
        }
        if (retries > 0) {
            for (int i = 0; i < retries; i++) {
                result = lock(key, value, timeout);
                if (!result) {
                    // 可以考虑线程随眠
                    i++;
                }
            }
        }
        return result;
    }

    /**
     * 设置锁，设置过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public boolean lock(String key, String value, long timeout) {
        return tryLock(key, value, timeout, TimeUnit.SECONDS, 1, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取分布式锁,并设置获取锁的超时时间
     *
     * @param key                分布式锁 key
     * @param value              分布式锁 value
     * @param expireTime         锁的超时时间,防止死锁
     * @param expireTimeUnit     锁的超时时间单位
     * @param acquireTimeout     尝试获取锁的等待时间,如果在时间范围内获取锁失败,就结束获取锁
     * @param acquireTimeoutUnit 尝试获取锁的等待时间单位
     * @return 是否成功获取分布式锁
     */
    public boolean tryLock(String key, String value, long expireTime, TimeUnit expireTimeUnit, int acquireTimeout, TimeUnit acquireTimeoutUnit) {
        try {
            // 尝试自旋获取锁,等待配置的一段时间,如果在时间范围内获取锁失败,就结束获取锁
            long end = System.currentTimeMillis() + acquireTimeoutUnit.toMillis(acquireTimeout);
            while (System.currentTimeMillis() < end) {
                // 尝试获取锁
                Boolean result = redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX + key, value, expireTime, expireTimeUnit);
                // 验证是否成功获取锁
                if (Objects.equals(Boolean.TRUE, result)) {
                    log.info("tryLock success   {}  {}  ", key, value);
                    return true;
                }
                // 睡眠 50 毫秒
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("tryLock {} {} error {}", key, value, e.getMessage());
        }
        log.info("tryLock fail  {}  {}  ", key, value);
        return false;
    }


    public boolean unlock(String key, String value) {
        String script = "if redis.call('get',KEYS[1]) == ARGV[1]"
                + "then"
                + "     return redis.call('del',KEYS[1])"
                + "else "
                + "     return 0 "
                + "end";
        String[] args = new String[]{value};
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX + key), args);
        if (Objects.equals(result, 1L)) {
            log.info("unlock ok");
            return true;
        }
        return false;
    }

}

package cn.z201.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class DistributedLockRedisTool {

    private static String LOCK_PREFIX = "lock:";

    @Autowired
    private RedisTemplate redisTemplate;

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

    public boolean lock(String key, String value, long timeout) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX + key, value, Duration.ofSeconds(timeout));
        if (result) {
            log.info("lock ok");
        }
        return result;
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

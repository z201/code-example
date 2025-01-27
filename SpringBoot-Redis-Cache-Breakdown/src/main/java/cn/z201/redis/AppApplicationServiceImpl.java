package cn.z201.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class AppApplicationServiceImpl {

    public static final String INFO = "INFO:";

    private static String LOCK_PREFIX = "LOCK:P";

    private final JdbcTemplate jdbcTemplate;

    private final RedisTemplate redisTemplate;

    @Autowired
    public AppApplicationServiceImpl(JdbcTemplate jdbcTemplate, RedisTemplate redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据用户组件获取数据
     *
     * @param id
     * @return
     */
    public Map<String, Object> findById(String id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        Map<String, Object> person = jdbcTemplate.queryForMap(sql, id);
        return person;
    }

    public List<Map<String, Object>> all() {
        List<Map<String, Object>> personList =
                jdbcTemplate.queryForList("SELECT * FROM person");
        return personList;
    }

    private boolean lock(String key, long timeout) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX, key, Duration.ofSeconds(timeout));
        if (result) {
            log.info("lock");
        }
        return result;
    }

    private boolean unlock(String key) {
        String script = "if redis.call('get',KEYS[1]) == ARGV[1]"
                + "then"
                + "     return redis.call('del',KEYS[1])"
                + "else "
                + "     return 0 "
                + "end";
        String[] args = new String[]{key};
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX), args);
        if (Objects.equals(result, 1L)) {
            log.info("unlock ok");
            return true;
        }
        return false;
    }

    public Map<String, Object> findCacheById(String id, Long lockOutTime, Long emptyOutTime) {
        Map<String, Object> result = new HashMap<>();
        Gson gson = new GsonBuilder().create();
        String key = INFO + id;
        Object data = redisTemplate.opsForValue().get(key);
        if (data == null) {
            try {
                if (lock(id, lockOutTime)) {
                    log.info("init data ");
                    result = findById(id);
                    if (CollectionUtils.isEmpty(result)) {
                        // 插入一个空格进去
                        log.info("set empty ");
                        result = new HashMap<>();
                        redisTemplate.opsForValue().set(key, gson.toJson(result), emptyOutTime, TimeUnit.MILLISECONDS);
                    } else {
                        log.info("set data ");
                        redisTemplate.opsForValue().set(key, gson.toJson(result));
                    }
                }
            } finally {
                unlock(id);
            }
        } else {
            result = gson.fromJson(data.toString(), Map.class);
            if (!CollectionUtils.isEmpty(result)) {
                log.info("get cache");
            }
        }
        return result;
    }
}

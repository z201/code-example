package cn.z201.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    public static final String INFO = "INFO:";

    private static String LOCK_PREFIX = "LOCK:P";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    public volatile ReentrantLock lock = new ReentrantLock();

    /**
     * 根据用户组件获取数据
     *
     * @param id
     * @return
     */
    private Map<String, Object> findById(String id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        Map<String, Object> person = jdbcTemplate.queryForMap(sql, id);
        return person;
    }

    private List<Map<String, Object>> all() {
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


    private void clean() {
        Set<String> keys =
                redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }


    @Test
    @Disabled
    public void dataView() {
        List<Map<String, Object>> personList =
                all();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("personList \n {}", gson.toJson(personList));
        Map<String, Object> objectMap = findById(personList.get(0).get("id").toString());
        log.info("person \n {}", gson.toJson(objectMap));
    }

    private Map<String, Object> findCacheById(String id,Long timeout) {
        Map<String, Object> result = new HashMap<>();
        Gson gson = new GsonBuilder().create();
        String key = INFO + id;
        Object data = redisTemplate.opsForValue().get(key);
        if (data == null) {
            try {
                if (lock(id, timeout)) {
                    log.info("init data ");
                    result = findById(id);
                    if (CollectionUtils.isEmpty(result)) {
                        // 插入一个空格进去
                        log.info("set empty ");
                        result = new HashMap<>();
                        redisTemplate.opsForValue().set(key, gson.toJson(result), 500, TimeUnit.MILLISECONDS);
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

    private void testCacheBreakdownInfo() {
        int count = 10;
        String id = "3";
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                log.info(" user {} ", findCacheById(id,1000L));
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();


    }

    @Test
    @Disabled
    public void test() throws InterruptedException {
        clean();
        for (int i = 0; i < 2; i++) {
            long startTimes = System.currentTimeMillis();
            testCacheBreakdownInfo();
            long endTimes = System.currentTimeMillis();
            long runTime = (endTimes - startTimes);
            log.info("执行完毕:  {}ms", runTime);
            Thread.sleep(1000L);
        }
        clean();
    }

}
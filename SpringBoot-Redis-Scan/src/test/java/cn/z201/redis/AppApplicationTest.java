package cn.z201.redis;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    private ScanTool scanTool;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 批量获取
     * @param keys
     * @return
     */
    public Map<String, String> batchGet(List<String> keys) {
        Map<String, String> saveMap = new HashMap<>();
        redisTemplate.executePipelined(
                (RedisCallback<String>) connection -> {
                    RedisSerializer<String> stringRedisSerializer
                            = RedisSerializer.string();
                    for (String key : keys) {
                        Object o = connection.get(stringRedisSerializer.serialize(key));
                        if (null != o) {
                            saveMap.put(key, String.valueOf(o));
                        }
                    }
                    return null;
                });
        return saveMap;
    }

    /**
     * string 批量写入
     * @param saveMap
     * @param unit
     * @param timeout
     */
    public void batchInsert(Map<String, String> saveMap, TimeUnit unit, int timeout) {
        /* 插入多条数据 */
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                Iterator<Map.Entry<String, String>> iterator = saveMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    redisTemplate.opsForValue().set(entry.getKey(), entry.getValue(), timeout, unit);
                }
                return null;
            }
        });
    }

    /**
     * set 批量写入
     * @param key
     * @param list
     * @return
     */
    public Boolean sAdd(String key, List<String> list) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                    RedisSerializer<String> stringRedisSerializer
                            = RedisSerializer.string();
                    byte[][] dataArr = new byte[list.size()][];
                    int count = list.size();
                    for (int i = 0; i < count; i++) {
                        dataArr[i] = stringRedisSerializer.serialize(list.get(i));
                    }
                    connection.sAdd(stringRedisSerializer.serialize(key), dataArr);
                    return true;
                }
        );
    }

    /**
     * hash批量写入
     * @param key
     * @param fieldMap
     * @return
     */
    public Boolean hMSet(String key, Map fieldMap) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                    RedisSerializer<String> stringRedisSerializer
                            = RedisSerializer.string();
                    Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
                    String mapKey;
                    Object value;
                    Map<byte[], byte[]> hashes = new LinkedHashMap(fieldMap.size());
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> entry = iterator.next();
                        mapKey = entry.getKey();
                        value = entry.getValue();
                        hashes.put(stringRedisSerializer.serialize(mapKey),
                                stringRedisSerializer.serialize(value.toString()));
                    }
                    connection.hMSet(stringRedisSerializer.serialize(key), hashes);
                    return true;
                }
        );
    }

    /**
     * zset 批量写入
     * @param key
     * @param fieldMap
     * @return
     */
    public Boolean zMSet(String key, Map<String, Long> fieldMap) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                    RedisSerializer<String> stringRedisSerializer
                            = RedisSerializer.string();
                    Iterator<Map.Entry<String, Long>> iterator = fieldMap.entrySet().iterator();
                    String value;
                    Long score;
                    Set<RedisZSetCommands.Tuple> tuples = new HashSet<>();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Long> entry = iterator.next();
                        value = entry.getKey();
                        score = entry.getValue();
                        tuples.add(new DefaultTuple(stringRedisSerializer.serialize(value), score.doubleValue()));
                    }
                    connection.zAdd(stringRedisSerializer.serialize(key), tuples);
                    return true;
                }
        );
    }

    @BeforeEach
    public void init() {
        int count = 100;
        Map<String, String> saveMap = new HashMap<>(1000);
        Map<String, Long> save = new HashMap<>(1000);
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String key = "user:" + i;
            keys.add(key);
            saveMap.put(key, String.valueOf(i));
            save.put(key, Long.valueOf(i));
        }
        batchInsert(saveMap, TimeUnit.MINUTES, 10);
        hMSet("mUser", saveMap);
        zMSet("zUser", save);
        sAdd("sUser", saveMap.values().stream().collect(Collectors.toList()));
    }

    @AfterEach
    public void clean() {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushAll();
            return null;
        });
    }

    @Test
    @Disabled
    public void test() {
        Set<String> keys = scanTool.scan("user:*", 10);
        log.info("{}", keys.size());
        log.info("{}", keys);
        Map<String, String> userMap = scanTool.hScan("mUser", "user:1*", 10);
        log.info("{}", userMap.size());
        log.info("{}", userMap);
        Map<String, Long> userZMap = scanTool.zScan("zUser", "user:1*", 10);
        log.info("{}", userZMap.size());
        log.info("{}", userZMap);
        Set<String> userSMap = scanTool.sScan("sUser", "1*", 10);
        log.info("{}", userSMap.size());
        log.info("{}", userSMap);

    }
}
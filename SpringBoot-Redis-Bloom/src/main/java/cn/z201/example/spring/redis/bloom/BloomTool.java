package cn.z201.example.redis.bloom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Slf4j
public class BloomTool {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 初始化
     * @param key
     * @param rate 准确度
     * @param size 大小
     */
    public Boolean init(String key, String rate, String size) {
        try {
            String script = "return redis.call('bf.reserve', KEYS[1], ARGV[1], ARGV[2])";
            DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<String>();
            defaultRedisScript.setResultType(String.class);
            defaultRedisScript.setScriptSource(new StaticScriptSource(script));
            String[] argv = new String[] { rate, size };
            String result = (String) redisTemplate.execute(defaultRedisScript, Collections.singletonList(key), argv);
            if (Objects.equals(result, "OK")) {
                return true;
            }
        }
        catch (RedisSystemException redisSystemException) {
            log.error("{}", redisSystemException.getMessage());
        }
        return false;
    }

    /**
     * 添加元素
     * @param key
     * @param value
     */
    public Boolean add(String key, String value) {
        String script = "return redis.call('bf.add', KEYS[1], ARGV[1])";
        return lua(script, key, value);
    }

    /**
     * 是否存在
     * @param key
     * @param value
     * @return
     */
    public Boolean exists(String key, Object value) {
        String script = "return redis.call('bf.exists', KEYS[1], ARGV[1])";
        return lua(script, key, value);
    }

    private Boolean lua(String script, String key, Object value) {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<Long>();
        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setScriptSource(new StaticScriptSource(script));
        Long result = (Long) redisTemplate.execute(defaultRedisScript, Collections.singletonList(key), value);
        if (Objects.equals(result, 1L)) {
            return true;
        }
        return false;
    }

}

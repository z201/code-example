package cn.z201.test.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Properties;

/**
 * @author z201.coding@gmail.com
 **/
@Repository
public class RedisVersionRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    public String version() {
        Properties info = redisTemplate.getConnectionFactory().getConnection().info();
        return info.get("redis_version").toString();
    }

}

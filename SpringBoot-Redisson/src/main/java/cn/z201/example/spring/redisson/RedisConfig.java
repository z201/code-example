package cn.z201.example.redisson;

import lombok.AllArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisProperties;

import java.io.IOException;

/**
 * @author z201.coding@gmail.com
 * @date 2021/12/24
 **/
@Configuration
public class RedisConfig {

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key序列化方式
        redisTemplate.setKeySerializer(redisSerializer);
        // value序列化
        redisTemplate.setValueSerializer(redisSerializer);
        // value hashmap序列化
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient() throws IOException {
        // Config config =
        // Config.fromYAML(RedisConfig.class.getClassLoader().getResource("redisson.yml"));
        // return Redisson.create(config);
        Config config = new Config();
        // 主从
        config.useMasterSlaveServers()
                // 可以用"rediss://"来启用SSL连接
                .setMasterAddress("redis://127.0.0.1:6379").setPassword("redis_pwd")
                .addSlaveAddress("redis://127.0.0.1:6380", "redis://127.0.0.1:6381").setPassword("redis_pwd")
                .setRetryInterval(5000).setTimeout(10000).setConnectTimeout(10000);// （连接超时，单位：毫秒
                                                                                   // 默认值：3000）;
        return Redisson.create(config);
    }

}

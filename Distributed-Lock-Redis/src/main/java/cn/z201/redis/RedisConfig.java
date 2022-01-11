
package cn.z201.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author z201.coding@gmail.com
 * @date 2021/12/24
 **/
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate template = new RedisTemplate();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        template.setConnectionFactory(connectionFactory);
        // key序列化方式
        template.setKeySerializer(redisSerializer);
        // value序列化
        template.setValueSerializer(redisSerializer);
        // value hashmap序列化
        template.setHashValueSerializer(redisSerializer);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }
}

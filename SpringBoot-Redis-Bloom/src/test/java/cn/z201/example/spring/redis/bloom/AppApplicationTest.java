package cn.z201.example.spring.redis.bloom;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    BloomTool bloomTool;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @Disabled
    public void testBloom() {
        Set<String> keys = redisTemplate.keys("*");
        log.info("{}", keys);
        redisTemplate.delete(keys);
        String key = "u";
        log.info("init 异常或者错误都会报错 {}", bloomTool.init(key, "0.001", "10000000"));
        List<String> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Integer value = RandomUtil.randomInt(1000, 20000000);
            values.add(value.toString());
            Boolean result = bloomTool.add(key, value.toString());
            log.info("bf.add {} {} {} ", result, key, value);
        }
        Integer index = RandomUtil.randomInt(0, values.size());
        String value = values.get(index);
        Boolean result = bloomTool.exists(key, value);
        log.info("bf.exists {} {} {} ", result, key, value);
    }

}
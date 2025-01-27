package cn.z201.example.distributed;

import cn.z201.example.distributed.redis.AppApplication;
import cn.z201.example.distributed.redis.DistributedLockRedisTool;
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

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    DistributedLockRedisTool distributedLockRedisTool;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @Disabled
    public void testLock() {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        String key = UUID.randomUUID().toString();
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        try {
            for (int i = 0; i < count; i++) {
                executorService.execute(() -> {
                    log.info(" lock {} key  {} ", distributedLockRedisTool.lock(key, key, 10L), key);
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<String> keys = redisTemplate.keys("*");
        log.info("keys {}", keys.toString());
        for (String item : keys) {
            item = item.replace("lock:","");
            log.info("unlock {} {}", distributedLockRedisTool.unlock(item, item),item);
        }
        executorService.shutdown();
    }


    @Test
    @Disabled
    public void delKeys() {
        Set<String> keys = redisTemplate.keys("*");
        log.info("keys {}", keys.toString());
        redisTemplate.delete(keys);
    }
}
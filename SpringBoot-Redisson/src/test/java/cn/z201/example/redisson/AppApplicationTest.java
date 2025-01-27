package cn.z201.example.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    private RedissonClient redissonClient;

    private static final String LOCK_TITLE = "redisLock_";

    @Test
    @Disabled
    void contextLoads() throws InterruptedException {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        String key = UUID.randomUUID().toString();
        String lockKey = LOCK_TITLE + key;
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
                    RLock lock = redissonClient.getLock(lockKey);
                    //加锁，并且设置锁过期时间，防止死锁的产生
                    Boolean result = lock.tryLock(2, TimeUnit.MINUTES);
                    log.info(" {} lock {}", result, lockKey);
                    Thread.sleep(1000L);
                    //执行具体业务逻辑
                    redissonClient.getBucket("value").set(String.valueOf(System.currentTimeMillis()));
                    String value = (String) redissonClient.getBucket("value").get();
                    log.info(" {} value {}", Thread.currentThread().getName(), value);
                } catch (Exception e) {
                    log.error("{}", e.getMessage());
                } finally {
                    //获取所对象
                    RLock lock = redissonClient.getLock(lockKey);
                    //释放锁（解锁）
                    lock.unlock();
                    log.info(" {} unlock {}", Thread.currentThread().getName(), lockKey);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.countDown();
        executorService.shutdown();
        ;
        log.info("run end~~~");
        Thread.sleep(11000L);
    }


}
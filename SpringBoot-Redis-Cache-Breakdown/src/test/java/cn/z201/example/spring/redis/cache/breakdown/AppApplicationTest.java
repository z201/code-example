package cn.z201.redis;

import cn.z201.example.spring.redis.cache.breakdown.AppApplication;
import cn.z201.example.spring.redis.cache.breakdown.AppApplicationServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.util.*;
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
    private RedisTemplate redisTemplate;

    @Autowired
    AppApplicationServiceImpl appApplicationService;

    @Test
    @Disabled
    public void dataView() {
        List<Map<String, Object>> personList = appApplicationService.all();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("personList \n {}", gson.toJson(personList));
        Map<String, Object> objectMap = appApplicationService.findById(personList.get(0).get("id").toString());
        log.info("person \n {}", gson.toJson(objectMap));
    }

    private void clean() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    private void testCacheBreakdownInfo() {
        int count = 10;
        String id = "3";
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                log.info(" user {} ", appApplicationService.findCacheById(id, 1000L, 10000L));
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        }
        catch (InterruptedException e) {
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
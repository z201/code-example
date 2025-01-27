package cn.z201.distributed.lock;

import cn.z201.example.distributed.lock.AppApplication;
import cn.z201.example.distributed.lock.DistributedLockTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    DistributedLockTool distributedLockTool;

    @Test
    @Disabled
    public void lock() {
        int count = 10;
        String key = UUID.randomUUID().toString();
        String value = UUID.randomUUID().toString();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                Boolean result = distributedLockTool.lock(key, value);
                log.info("lock result {} {} {} ", result, key, value);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        unLock(key,value);
    }

    public void unLock(String key,String value) {
        Boolean result = distributedLockTool.unLock(key,value);
        log.info("unLock result {} {}  ", result, key);
    }
}
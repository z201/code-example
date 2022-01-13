package cn.z201.zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.*;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    DistributedLockZookeeperCuratorTool distributedLockZookeeperCuratorTool;

    @Resource(name = "curatorFramework")
    private CuratorFramework curatorFramework;

    @Test
    @Disabled
    public void testInterProcessMutexLock() {
        int count = 10;
        String key = UUID.randomUUID().toString();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        try {
            for (int i = 0; i < count; i++) {
                executorService.execute(() -> {
                    String path = distributedLockZookeeperCuratorTool.createPathKey(key);
                    InterProcessMutex lock = new InterProcessMutex(curatorFramework, path);
                    try {
                        lock.acquire();
                        log.info("{} 获取的到锁", Thread.currentThread().getName());
                        Thread.sleep(2000);
                        lock.release();
                        log.info("{} 释放锁", Thread.currentThread().getName());
                        countDownLatch.countDown();
                    } catch (Exception e) {

                    } finally {
                        try {
                            lock.release();
                        } catch (Exception e) {
//                            log.error("release error {}",e.getMessage());
                        }
                    }
                });
            }
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Disabled
    public void testInterProcessSemaphoreMutexLock() {
        int count = 10;
        String key = UUID.randomUUID().toString();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        try {
            for (int i = 0; i < count; i++) {
                executorService.execute(() -> {
                    String path = distributedLockZookeeperCuratorTool.createPathKey(key);
                    InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(curatorFramework, path);
                    try {
                        lock.acquire();
                        log.info("{} 获取的到锁", Thread.currentThread().getName());
                        Thread.sleep(2000);
                        lock.release();
                        log.info("{} 释放锁", Thread.currentThread().getName());
                        countDownLatch.countDown();
                    } catch (Exception e) {

                    } finally {
                        try {
                            lock.release();
                        } catch (Exception e) {
//                            log.error("release error {}",e.getMessage());
                        }
                    }
                });
            }
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
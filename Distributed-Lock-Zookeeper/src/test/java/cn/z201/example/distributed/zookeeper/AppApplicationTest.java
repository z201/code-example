package cn.z201.example.distributed.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
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

    @Resource
    private ZooKeeper zkClient;

    @Test
    @Disabled
    public void testLock() throws InterruptedException, KeeperException {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        try {
            for (int i = 0; i < count; i++) {
                executorService.execute(() -> {
                    try {
                        DistributedLockZookeeperTool distributedLockZookeeperTool = new DistributedLockZookeeperTool(
                                zkClient);
                        Thread.sleep(1000);
                        log.info(" lock {} ", distributedLockZookeeperTool.tryLock());
                        distributedLockZookeeperTool.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        DistributedLockZookeeperTool distributedLockZookeeperTool = new DistributedLockZookeeperTool(zkClient);
        log.info("{}", distributedLockZookeeperTool.list());
    }

}
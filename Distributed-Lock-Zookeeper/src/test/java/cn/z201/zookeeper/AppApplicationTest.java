package cn.z201.zookeeper;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    DistributedLockZookeeperTool distributedLockZookeeperTool;

    @Test
    @Disabled
    public void testExists() {
        Stat stat = distributedLockZookeeperTool.exists("/node", new ZookeeperWatcher());
        if (Validator.isNotNull(stat)) {
            log.info("stat {}", JsonTool.toString(stat));
        }
    }

    @Test
    @Disabled
    public void createNode() {
        Boolean result = distributedLockZookeeperTool.createNode("/node", "1");
        log.info("result {}", result);
    }

    @Test
    @Disabled
    public void updateNode() {
        Boolean result = distributedLockZookeeperTool.createNode("/node", "2");
        log.info("result {}", result);
    }

    @Test
    @Disabled
    public void deleteNode() {
        Boolean result = distributedLockZookeeperTool.deleteNode("/node");
        log.info("result {}", result);
    }

    @Test
    @Disabled
    public void getChildren() throws InterruptedException, KeeperException {
        List<String> result = distributedLockZookeeperTool.getChildren("/node");
        log.info("result {}", result);
    }

    @Test
    @Disabled
    public void getData() {
        String result = distributedLockZookeeperTool.getData("/node",new ZookeeperWatcher());
        log.info("result {}", result);
    }

    @Test
    @Disabled
    public void testLock() {
        int count = 10;
        String key = UUID.randomUUID().toString();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        try {
            for (int i = 0; i < count; i++) {
                executorService.execute(() -> {
                    log.info(" lock {} key  {} ", distributedLockZookeeperTool.createNode("/"+key, key),key);
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

}
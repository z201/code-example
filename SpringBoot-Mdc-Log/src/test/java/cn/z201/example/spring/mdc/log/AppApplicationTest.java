package cn.z201.example.spring.mdc.log;

import cn.z201.example.spring.mdc.log.AppApplication;
import cn.z201.example.spring.mdc.log.MdcThreadTaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Test
    @Disabled
    public void testMdcTask() {
        MdcThreadTaskUtils.run(() -> {
            log.info("");
        });
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            MdcThreadTaskUtils.run(() -> {
                log.info("{}", Thread.currentThread().getId());
                countDownLatch.countDown();
            });
        }
        countDownLatch.countDown();
    }

}
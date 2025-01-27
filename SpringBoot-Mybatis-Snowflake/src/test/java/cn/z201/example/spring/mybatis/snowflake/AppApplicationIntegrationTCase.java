package cn.z201.example.snowflake;

import cn.z201.example.mybatis.snowflake.AccountToolService;
import cn.z201.example.mybatis.snowflake.AppApplication;
import cn.z201.example.mybatis.snowflake.dao.AccountDao;
import cn.z201.example.mybatis.snowflake.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test") // 修改这里，使用 @ActiveProfiles 替代 @Profile
public class AppApplicationIntegrationTCase {

    @Resource
    AccountDao accountDao;

    @Autowired
    AccountToolService accountToolService;

    @Test
    @Disabled
    public void initData() {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                accountDao.insert(accountToolService.create());
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
        List<Account> accountList = accountDao.selectList(null);
        for (Account account : accountList) {
            log.info("account : \n {}", account);
        }
    }

}
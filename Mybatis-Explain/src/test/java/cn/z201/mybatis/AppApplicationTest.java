package cn.z201.mybatis;

import cn.z201.mybatis.dao.AccountDao;
import cn.z201.mybatis.entity.Account;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Resource
    AccountDao accountDao;

    @Test
    @Disabled
    public void setup(){
        for (int i = 0; i < 1000; i++) {
            accountDao.insert(TestTool.create());
        }
    }



    @Test
    @Disabled
    public void select(){
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.select("id");
        accountQueryWrapper.eq("id",1);
        accountQueryWrapper.last("LIMIT 0 , 100");
        accountDao.selectList(accountQueryWrapper);
    }

}
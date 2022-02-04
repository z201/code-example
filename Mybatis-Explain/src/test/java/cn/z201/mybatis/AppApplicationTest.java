package cn.z201.mybatis;

import cn.z201.mybatis.dao.AccountDao;
import cn.z201.mybatis.entity.Account;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
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
import java.time.Clock;
import java.util.Locale;

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
            accountDao.insert(create());
        }
    }

    private Account create(){
        Fairy fairy = Fairy.create(Locale.CHINA);
        //设置生成年龄在21到27岁
        Person person = fairy.person(
                PersonProperties.ageBetween(18, 29),
                PersonProperties.telephoneFormat("13#########"));//设置手机号码格式
        String fullName = person.getFullName().replace(" ", "");
        String email = "";
        try {
            email = PinyinHelper.convertToPinyinString(person.getEmail(), "", PinyinFormat.WITHOUT_TONE);
            //EMail
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        Long time = Clock.systemDefaultZone().millis();
        Account account = new Account();
        account.setIsEnable(true);
        account.setCreateTime(time);
        account.setUpdateTime(time);
        account.setUsrName(fullName);
        account.setEmail(email);
        account.setPhoneNumber(person.getTelephoneNumber());
        account.setSaltPassword(person.getPassword());
        return account;
    }

    @Test
    @Disabled
    public void select(){
        accountDao.selectList(null);
    }

}
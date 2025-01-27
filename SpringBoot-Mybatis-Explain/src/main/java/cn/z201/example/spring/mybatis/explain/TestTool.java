package cn.z201.example.mybatis.explain;

import cn.z201.example.mybatis.explain.entity.Account;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;

import java.time.Clock;
import java.util.Locale;

/**
 * @author z201.coding@gmail.com
 **/
public class TestTool {

    public static Account create() {
        Fairy fairy = Fairy.create(Locale.CHINA);
        // 设置生成年龄在21到27岁
        Person person = fairy.person(PersonProperties.ageBetween(18, 29),
                PersonProperties.telephoneFormat("13#########"));// 设置手机号码格式
        String fullName = person.getFullName().replace(" ", "");
        String email = "";
        try {
            email = PinyinHelper.convertToPinyinString(person.getEmail(), "", PinyinFormat.WITHOUT_TONE);
            // EMail
        }
        catch (PinyinException e) {
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

}

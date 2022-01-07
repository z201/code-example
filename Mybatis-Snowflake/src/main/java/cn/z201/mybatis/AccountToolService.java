package cn.z201.mybatis;

import cn.z201.mybatis.entity.Account;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @author z201.coding@gmail.com
 * @date 2022/1/8
 **/
@Service
@Slf4j
public class AccountToolService {

    public Account create(){
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
        Account account = new Account();
        account.setUsrName(fullName);
        account.setEmail(email);
        account.setPhoneNumber(person.getTelephoneNumber());
        account.setSaltPassword(person.getPassword());
        return account;
    }
}

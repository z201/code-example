package cn.z201.email;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
public class AppApplicationTest {

    @Autowired
    private MailServiceI mailService;

    @Test
    @Disabled
    public void send(){
//        mailService.sendSimpleMail("z201.coding@gmail.com","测试邮件是否发送成功","这是一条测试邮件，用户测试内容是否正常显示。");
        Context context = new Context();
        context.setVariable("id", "882398087");
        mailService.sendSimpleMail("z201.coding@gmail.com","页面邮件",context);
    }

}
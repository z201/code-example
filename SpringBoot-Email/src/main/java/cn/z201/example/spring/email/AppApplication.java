package cn.z201.example.email;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication(scanBasePackages = "cn.z201.email")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }

}

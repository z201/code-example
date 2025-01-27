package cn.z201.example.blocking.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication(scanBasePackages = "cn.z201.blocking")
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }
}

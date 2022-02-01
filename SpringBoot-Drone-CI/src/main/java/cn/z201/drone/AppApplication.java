package cn.z201.drone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication(scanBasePackages = "cn.z201.drone")
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }

}

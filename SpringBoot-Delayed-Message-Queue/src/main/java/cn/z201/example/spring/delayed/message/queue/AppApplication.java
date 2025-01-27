package cn.z201.example.delayed.message.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication(scanBasePackages = "cn.z201.delayed")
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }

    @Bean("defExecutor")
    public ThreadPoolTaskExecutor defExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(5);
        executor.setKeepAliveSeconds(2);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("def-Executor-");
        return executor;
    }

}

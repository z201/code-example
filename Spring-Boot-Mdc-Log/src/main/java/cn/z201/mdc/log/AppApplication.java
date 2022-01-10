package cn.z201.mdc.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication
@EnableAsync
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = MdcThreadPoolTaskExecutor.newWithInheritedMdc(8, 32, 2, TimeUnit.MINUTES, 1000, new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("def-Executor ");
        return executor;
    }
}

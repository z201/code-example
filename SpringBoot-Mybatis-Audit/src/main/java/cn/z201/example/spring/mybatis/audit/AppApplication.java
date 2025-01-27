package cn.z201.example.mybatis.audit;


import cn.z201.example.mybatis.audit.config.mdc.MdcThreadPoolTaskExecutor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.z201.example.mybatis.audit.persistence.dao")
@EnableAspectJAutoProxy
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }

    @Bean("auditLogExecutor")
    public MdcThreadPoolTaskExecutor taskExecutor() {
        MdcThreadPoolTaskExecutor executor = MdcThreadPoolTaskExecutor.newWithInheritedMdc(
                1,
                5,
                2, TimeUnit.MINUTES,
                200, new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("def-executor ");
        return executor;
    }
}


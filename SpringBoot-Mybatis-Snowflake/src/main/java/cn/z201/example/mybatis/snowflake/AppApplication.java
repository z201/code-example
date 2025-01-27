package cn.z201.example.mybatis.snowflake;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication
@MapperScan("cn.z201.example.mybatis.snowflake.dao")
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
    }
}

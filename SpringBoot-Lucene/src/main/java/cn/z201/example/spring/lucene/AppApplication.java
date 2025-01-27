package cn.z201.example.spring.lucene.lucene;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

/**
 * @author z201.coding@gmail.com
 */
@SpringBootApplication(scanBasePackages = "cn.z201.example.spring.lucene.lucene")
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppApplication.class, args);
        // 加载ik分词器配置 防止第一次查询慢
        Dictionary.initial(DefaultConfig.getInstance());
    }

}

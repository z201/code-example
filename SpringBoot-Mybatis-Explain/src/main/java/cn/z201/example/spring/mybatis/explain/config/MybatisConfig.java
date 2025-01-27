package cn.z201.example.spring.mybatis.explain.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author z201.coding@gmail.com
 * @date 2021/12/26
 **/
@Configuration
public class MybatisConfig {

    /**
     * #开启返回map结果集的下划线转驼峰
     * @return
     */
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }

    /**
     * 攻击 SQL 阻断解析器,防止全表更新与删除
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    // 可以直接在插件上增加Component 也可以在此处声明
    @Bean
    public String localInterceptor(SqlSessionFactory sqlSessionFactory) {
        // 实例化插件
        MybatisInterceptor sqlInterceptor = new MybatisInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
        return "interceptor";
    }

}

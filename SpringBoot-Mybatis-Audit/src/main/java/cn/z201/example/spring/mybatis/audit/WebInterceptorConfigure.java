package cn.z201.example.spring.mybatis.audit;

import cn.z201.example.spring.mybatis.audit.config.mdc.MdcTraceContextFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class WebInterceptorConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MdcTraceContextFilter());
    }

}

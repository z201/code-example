package cn.z201.example.spring.spi;

import cn.z201.example.jdk.spi.SpeakSPIService;
import org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class SpeakSPIServiceConfig {

    @Bean("initBeanFactoryServiceLoaderFactoryBean")
    public ServiceLoaderFactoryBean serviceLoaderFactoryBean() {
        ServiceLoaderFactoryBean serviceLoaderFactoryBean = new ServiceLoaderFactoryBean();
        serviceLoaderFactoryBean.setServiceType(SpeakSPIService.class);
        return serviceLoaderFactoryBean;
    }

}

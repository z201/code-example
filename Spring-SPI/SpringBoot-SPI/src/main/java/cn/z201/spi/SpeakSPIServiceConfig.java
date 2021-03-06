package cn.z201.spi;

import org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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

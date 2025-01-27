package cn.z201.example.spring.programming.model.initialization;

import org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class ServiceLoaderFactoryBeanConfig {

    @Bean("initBeanFactoryServiceLoaderFactoryBean")
    public ServiceLoaderFactoryBean serviceLoaderFactoryBean() {
        ServiceLoaderFactoryBean serviceLoaderFactoryBean = new ServiceLoaderFactoryBean();
        serviceLoaderFactoryBean.setServiceType(InitFactory.class);
        return serviceLoaderFactoryBean;
    }

}

package cn.z201.example.spring.aop.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class JsonConfiguration {

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        Collection<HttpMessageConverter<?>> converters = new ArrayList<>();
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        gsonHttpMessageConverter.setGson(gson);
        converters.add(gsonHttpMessageConverter);
        return new HttpMessageConverters(true, converters);
    }

}

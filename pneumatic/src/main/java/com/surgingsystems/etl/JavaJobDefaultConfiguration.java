package com.surgingsystems.etl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JavaJobDefaultConfiguration {

    @Bean(name = "conversionService")
    public ConversionServiceFactoryBean conversionService() {
        return new ConversionServiceFactoryBean();
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
    
}

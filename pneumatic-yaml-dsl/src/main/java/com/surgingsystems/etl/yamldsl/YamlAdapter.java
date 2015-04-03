package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

interface YamlAdapter {

    Class<?> getTargetType();

    void adapt(BeanDefinitionBuilder builder, String toProperty, Object value, ApplicationContext applicationContext);

    void adaptBuilder(BeanDefinitionBuilder builder, String toProperty, BeanDefinitionBuilder value,
            ApplicationContext applicationContext);
}
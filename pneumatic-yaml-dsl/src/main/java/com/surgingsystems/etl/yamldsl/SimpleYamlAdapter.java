package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

abstract class SimpleYamlAdapter implements YamlAdapter {

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        adapt(builder, toProperty, value, applicationContext);
    }

    @Override
    public void adaptBuilder(BeanDefinitionBuilder builder, String toProperty, BeanDefinitionBuilder value,
            ApplicationContext applicationContext) {
        adapt(builder, toProperty, value.getBeanDefinition(), applicationContext);
    }
}

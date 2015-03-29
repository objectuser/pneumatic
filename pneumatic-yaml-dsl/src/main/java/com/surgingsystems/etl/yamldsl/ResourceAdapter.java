package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class ResourceAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return Resource.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        String location = (String) value;
        Resource resource = applicationContext.getResource(location);
        builder.addPropertyValue(toProperty, resource);
    }
}

package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

public class PropertyNameAdapter implements YamlAdapter {

    private String propertyName;

    public PropertyNameAdapter(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        builder.addPropertyValue(propertyName, value);
    }

}

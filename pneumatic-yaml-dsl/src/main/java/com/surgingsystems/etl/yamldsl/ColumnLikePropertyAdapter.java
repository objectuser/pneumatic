package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.schema.ColumnDefinition;

public class ColumnLikePropertyAdapter implements YamlAdapter {

    private String property;

    public ColumnLikePropertyAdapter(String property) {
        this.property = property;
    }

    @Override
    public Class<?> getTargetType() {
        return ColumnDefinition.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        builder.addPropertyValue(property, value);
    }

}

package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.schema.ColumnType;

class ColumnTypeAdapter extends SimpleYamlAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return Void.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        ColumnType<?> type = null;
        if ("string".equals(value)) {
            type = applicationContext.getBean("stringColumnType", ColumnType.class);
        } else if ("integer".equals(value)) {
            type = applicationContext.getBean("integerColumnType", ColumnType.class);
        } else if ("decimal".equals(value)) {
            type = applicationContext.getBean("decimalColumnType", ColumnType.class);
        } else {
            throw new IllegalArgumentException("what's a " + value + "?");
        }

        builder.addPropertyValue(toProperty, type);
    }
}
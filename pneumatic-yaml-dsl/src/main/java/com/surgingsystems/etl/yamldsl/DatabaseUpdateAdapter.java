package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.filter.database.ConfigurableDatabaseWriteStrategy;

class DatabaseUpdateAdapter extends SimpleYamlAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return ConfigurableDatabaseWriteStrategy.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        builder.addPropertyValue("writeStrategy", value);
    }

}

package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.surgingsystems.etl.filter.FlatFileRecordReader;

class FlatFileRecordReaderAdapter extends SimpleYamlAdapter implements YamlAdapter {

    private String propertyName;

    public FlatFileRecordReaderAdapter(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Class<?> getTargetType() {
        return FlatFileRecordReader.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {

        FlatFileRecordReader itemReader = null;
        if (value instanceof String) {
            String location = (String) value;
            Resource resource = applicationContext.getResource(location);
            itemReader = new FlatFileRecordReader();
            itemReader.setResource(resource);

            builder.addPropertyValue(propertyName, itemReader);
        } else if (value instanceof BeanDefinition) {
            builder.addPropertyValue(propertyName, value);
        }
    }

}

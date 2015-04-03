package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;

class HttpMethodAdapter extends SimpleYamlAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        String method = (String) value;
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        builder.addPropertyValue(toProperty, httpMethod);
    }
}

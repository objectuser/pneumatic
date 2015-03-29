package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.record.SingleColumnComparator;
import com.surgingsystems.etl.schema.ColumnDefinition;

public class ComparatorAdapter implements YamlAdapter {

    private String property;

    public ComparatorAdapter(String property) {
        this.property = property;
    }

    @Override
    public Class<?> getTargetType() {
        return ColumnDefinition.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        BeanDefinitionBuilder comparatorBean = BeanDefinitionBuilder
                .genericBeanDefinition(SingleColumnComparator.class);
        comparatorBean.addPropertyValue("columnDefinition", value);
        builder.addPropertyValue(property, comparatorBean.getBeanDefinition());
    }

}

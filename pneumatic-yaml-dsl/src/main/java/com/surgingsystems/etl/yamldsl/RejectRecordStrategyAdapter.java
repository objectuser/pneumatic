package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.PipeRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;

class RejectRecordStrategyAdapter extends SimpleYamlAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return RejectRecordStrategy.class;
    }

    @Override
    public void adaptBuilder(BeanDefinitionBuilder builder, String toProperty, BeanDefinitionBuilder value,
            ApplicationContext applicationContext) {
        BeanDefinitionBuilder valueBuilder = value;

        if (valueBuilder.getRawBeanDefinition().getPropertyValues().contains("output")) {
            valueBuilder.getRawBeanDefinition().setBeanClass(PipeRejectRecordStrategy.class);
        } else {
            valueBuilder.getRawBeanDefinition().setBeanClass(LogRejectRecordStrategy.class);
        }
        builder.addPropertyValue("rejectRecordStrategy", valueBuilder.getBeanDefinition());
    }
}

package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.schema.ColumnType;

/**
 * Adapt the output column name and use the column type to set the summation
 * strategy.
 */
class SumOutputColumnAdapter extends ColumnLikePropertyAdapter {

    public SumOutputColumnAdapter(String property) {
        super(property);
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        super.adapt(builder, toProperty, value, applicationContext);

        BeanDefinition outputColumnDefinition = (BeanDefinition) value;
        ColumnType<?> type = (ColumnType<?>) outputColumnDefinition.getPropertyValues().get("type");

        if (Integer.class.equals(type.getCoreType())) {
            builder.addPropertyReference("sumStrategy", "sumIntegerStrategy");
        } else {
            builder.addPropertyReference("sumStrategy", "sumDecimalStrategy");
        }
    }

}

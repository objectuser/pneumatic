package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.filter.mapping.Mapping;

/**
 * This is crazy complicated. Might look for a better way to map this. Can we
 * use a factory bean?
 */
class MappingsAdapter extends SimpleYamlAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return ColumnMappingAdapter.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        ManagedList<BeanDefinition> mappings = (ManagedList<BeanDefinition>) value;
        ManagedMap<BeanDefinition, BeanDefinition> columnMappings = new ManagedMap<>();
        for (BeanDefinition columnMappingBean : mappings) {
            BeanDefinition from = (BeanDefinition) columnMappingBean.getPropertyValues().get("from");
            BeanDefinition to = (BeanDefinition) columnMappingBean.getPropertyValues().get("to");
            columnMappings.put(to, from);
        }
        BeanDefinitionBuilder mapping = BeanDefinitionBuilder.genericBeanDefinition(Mapping.class);
        mapping.addPropertyValue("columnDefinitionMappings", columnMappings);
        builder.addPropertyValue("mapping", mapping.getBeanDefinition());
    }
}

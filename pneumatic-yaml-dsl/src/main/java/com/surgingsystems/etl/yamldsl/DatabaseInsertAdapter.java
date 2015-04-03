package com.surgingsystems.etl.yamldsl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.filter.database.DatabaseWriteStrategy;
import com.surgingsystems.etl.filter.database.InsertDatabaseWriteStrategy;

class DatabaseInsertAdapter extends SimpleYamlAdapter implements YamlAdapter {

    @Override
    public Class<?> getTargetType() {
        return DatabaseWriteStrategy.class;
    }

    @Override
    public void adapt(BeanDefinitionBuilder builder, String toProperty, Object value,
            ApplicationContext applicationContext) {
        InsertDatabaseWriteStrategy writeStrategy = new InsertDatabaseWriteStrategy();
        writeStrategy.setTableName((String) value);
        builder.addPropertyValue("writeStrategy", writeStrategy);
    }

}

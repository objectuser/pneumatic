package com.surgingsystems.etl.builders;

import org.springframework.beans.factory.FactoryBean;

import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.ColumnType;

public class ColumnFactoryBean implements FactoryBean<ColumnDefinition<?>> {
    
    private String name;
    
    private ColumnType<?> columnType;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ColumnDefinition<?> getObject() throws Exception {
        return new ColumnDefinition(name, columnType);
    }

    @Override
    public Class<?> getObjectType() {
        return ColumnDefinition.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColumnType(ColumnType<?> columnType) {
        this.columnType = columnType;
    }

}

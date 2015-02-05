package com.surgingsystems.etl.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;

import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.ColumnType;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.TabularSchema;

public class SchemaFactoryBean implements FactoryBean<Schema> {

    private String name;

    private List<ColumnDefinition<? extends Comparable<?>>> columnDefinitions = new ArrayList<ColumnDefinition<? extends Comparable<?>>>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setColumns(Map<String, ColumnType<? extends Comparable<?>>> columns) {
        for (Map.Entry<String, ColumnType<? extends Comparable<?>>> entry : columns.entrySet()) {
            columnDefinitions.add(new ColumnDefinition(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public Schema getObject() throws Exception {
        return new TabularSchema(name, columnDefinitions);
    }

    @Override
    public Class<?> getObjectType() {
        return Schema.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package com.surgingsystems.etl.builders;

import java.util.LinkedHashMap;
import java.util.Map;

import com.surgingsystems.etl.filter.mapping.Mapping;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.ColumnType;

@SuppressWarnings("unused")
public class MappingsBuilder {

    private Map<ColumnDefinition<?>, ColumnDefinition<?>> map = new LinkedHashMap<ColumnDefinition<?>, ColumnDefinition<?>>();
    
    public Mapping build() {
        return null;
    }
    
    public void setColumnMappings(Map<String, ColumnType<? extends Comparable<?>>> columns) {
        for (Map.Entry<String, ColumnType<? extends Comparable<?>>> entry : columns.entrySet()) {
        }
    }

}

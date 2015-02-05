package com.surgingsystems.etl.schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;

public class TabularSchema implements Schema {

    private String name;

    private Map<String, ColumnDefinition<? extends Comparable<?>>> columnNameToDefinitionMap = new LinkedHashMap<String, ColumnDefinition<? extends Comparable<?>>>();

    public TabularSchema() {
    }

    public TabularSchema(String name) {
        setName(name);
    }

    public TabularSchema(String name, @SuppressWarnings("rawtypes") ColumnDefinition... columnDefinitions) {
        this(name, Arrays.asList(columnDefinitions));
    }

    public TabularSchema(String name, List<ColumnDefinition<? extends Comparable<?>>> columnDefinitions) {
        setName(name);

        for (ColumnDefinition<?> columnDefinition : columnDefinitions) {
            addColumnDefinition(columnDefinition);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<ColumnDefinition<? extends Comparable<?>>> iterator() {
        return getColumnDefinitions().iterator();
    }

    @Override
    public boolean contains(ColumnDefinition<?> columnDefinition) {
        return getColumnDefinitions().contains(columnDefinition);
    }

    @Override
    public ColumnDefinition<? extends Comparable<?>> getColumnForName(String name) {
        return columnNameToDefinitionMap.get(name);
    }

    @Override
    public Collection<ColumnDefinition<? extends Comparable<?>>> getColumnDefinitions() {
        return columnNameToDefinitionMap.values();
    }

    @Override
    public Record createEmptyRecord() {
        return new DataRecord(this);
    }

    public void addColumnDefinition(ColumnDefinition<?> columnDefinition) {
        columnNameToDefinitionMap.put(columnDefinition.getName(), columnDefinition);
    }

    public void setColumnDefinitions(Collection<ColumnDefinition<? extends Comparable<?>>> columnDefinitions) {
        for (ColumnDefinition<? extends Comparable<?>> columnDefinition : columnDefinitions) {
            addColumnDefinition(columnDefinition);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), getName(), columnNameToDefinitionMap.values());
    }
}

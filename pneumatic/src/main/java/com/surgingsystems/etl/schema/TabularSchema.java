package com.surgingsystems.etl.schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.util.Assert;

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

    @SafeVarargs
    public TabularSchema(String name, ColumnDefinition<? extends Comparable<?>>... columnDefinitions) {
        this(name, Arrays.asList(columnDefinitions));
    }

    public TabularSchema(String name, List<ColumnDefinition<? extends Comparable<?>>> columnDefinitions) {
        setName(name);

        for (ColumnDefinition<?> columnDefinition : columnDefinitions) {
            addColumn(columnDefinition);
        }
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.isTrue(!columnNameToDefinitionMap.isEmpty(), "Columns are required");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<ColumnDefinition<? extends Comparable<?>>> iterator() {
        return getColumns().iterator();
    }

    @Override
    public boolean contains(ColumnDefinition<?> columnDefinition) {
        return getColumns().contains(columnDefinition);
    }

    @Override
    public ColumnDefinition<? extends Comparable<?>> getColumnForName(String name) {
        return columnNameToDefinitionMap.get(name);
    }

    @Override
    public Collection<ColumnDefinition<? extends Comparable<?>>> getColumns() {
        return columnNameToDefinitionMap.values();
    }

    @Override
    public Record createEmptyRecord() {
        return new DataRecord(this);
    }

    public void addColumn(ColumnDefinition<?> columnDefinition) {
        columnNameToDefinitionMap.put(columnDefinition.getName(), columnDefinition);
    }

    public void setColumns(Collection<ColumnDefinition<? extends Comparable<?>>> columnDefinitions) {
        for (ColumnDefinition<? extends Comparable<?>> columnDefinition : columnDefinitions) {
            addColumn(columnDefinition);
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

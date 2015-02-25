package com.surgingsystems.etl.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.utility.Equality;

public abstract class RecordWithColumnDefaults implements Record {

    private Map<ColumnDefinition<?>, Column<?>> definitionToColumnMap = new LinkedHashMap<ColumnDefinition<?>, Column<?>>();

    private Map<String, Column<?>> nameToColumnMap = new LinkedHashMap<String, Column<?>>();

    public RecordWithColumnDefaults() {
    }

    public void addColumn(Column<?> column) {
        definitionToColumnMap.put(column.getColumnDefinition(), column);
        nameToColumnMap.put(column.getName(), column);
    }

    public Iterator<Column<?>> getColumnIterator() {
        return definitionToColumnMap.values().iterator();
    }

    @Override
    public Iterator<Column<?>> iterator() {
        return getColumnIterator();
    }

    @Override
    public <T extends Comparable<T>> T getValueForName(String columnName) {
        Column<T> column = getColumnForName(columnName);
        if (column != null) {
            return column.getValue();
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> Column<T> getColumnFor(ColumnDefinition<T> columnDefinition) {
        return (Column<T>) definitionToColumnMap.get(columnDefinition);
    }

    @Override
    public <T extends Comparable<T>> T getValueFor(ColumnDefinition<T> columnDefinition) {
        return getColumnFor(columnDefinition).getValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Comparable<T>> Column<T> getColumnForName(String columnName) {
        return (Column<T>) nameToColumnMap.get(columnName);
    }

    @Override
    public List<Column<?>> getColumns() {
        List<Column<?>> result = new ArrayList<Column<?>>();
        for (Map.Entry<ColumnDefinition<?>, Column<?>> columnEntry : definitionToColumnMap.entrySet()) {
            result.add(columnEntry.getValue());
        }
        return result;
    }

    public void setColumns(List<Column<?>> columns) {
        for (Column<?> column : columns) {
            addColumn(column);
        }
    }

    @Override
    public boolean hasColumnFor(ColumnDefinition<?> columnDefinition) {
        return definitionToColumnMap.containsKey(columnDefinition);
    }

    @Override
    public boolean hasColumnForName(String columnName) {
        return nameToColumnMap.containsKey(columnName);
    }

    @Override
    public void setColumn(Column<?> column) {
        definitionToColumnMap.put(column.getColumnDefinition(), column);
        nameToColumnMap.put(column.getName(), column);
    }

    @Override
    public int hashCode() {
        return definitionToColumnMap.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        RecordWithColumnDefaults other = Equality.applicable(RecordWithColumnDefaults.class, this, o);
        if (other == null) {
            return false;
        } else {
            return nameToColumnMap.equals(other.nameToColumnMap);
        }
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<String, String>();
        for (Column<?> column : getColumns()) {
            result.put(column.getName(), column.getValue() == null ? null : column.getValue().toString());
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), definitionToColumnMap);
    }
}

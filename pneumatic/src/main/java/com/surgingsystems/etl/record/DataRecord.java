package com.surgingsystems.etl.record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class DataRecord implements Record {

    private Map<ColumnDefinition<?>, Column<?>> definitionToColumnMap = new LinkedHashMap<ColumnDefinition<?>, Column<?>>();

    private Map<String, Column<?>> nameToColumnMap = new LinkedHashMap<String, Column<?>>();

    public DataRecord() {
    }

    /**
     * Create an empty record with columns from the schema.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DataRecord(Schema schema) {
        for (ColumnDefinition<?> columnDefinition : schema) {
            addColumn(new Column(columnDefinition, null));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DataRecord(Schema schema, Object... values) {
        if (schema.getColumnDefinitions().size() != values.length) {
            throw new IllegalArgumentException(String.format(
                    "Record has %d columns, %d values provided - there must be one value for each column definition",
                    schema.getColumnDefinitions().size(), values.length));
        }

        int i = 0;
        for (ColumnDefinition<?> columnDefinition : schema) {
            Comparable<?> value = null;
            if (values.length > i) {
                value = (Comparable<?>) values[i++];
            }
            addColumn(new Column(columnDefinition, value));
        }
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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Comparable<T>> T getValueForName(String columnName) {
        return (T) getColumnForName(columnName).getValue();
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
    public void setColumn(Column<?> column) {
        definitionToColumnMap.put(column.getColumnDefinition(), column);
        nameToColumnMap.put(column.getName(), column);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), definitionToColumnMap);
    }
}

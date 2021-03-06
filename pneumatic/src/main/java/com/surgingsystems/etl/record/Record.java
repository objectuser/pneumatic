package com.surgingsystems.etl.record;

import java.util.List;
import java.util.Map;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;

/**
 * An aggregation of columns.
 */
public interface Record extends Iterable<Column<?>> {

    <T extends Comparable<T>> T getValueFor(ColumnDefinition<T> columnDefinition);

    <T extends Comparable<T>> T getValueForName(String columnName);

    <T extends Comparable<T>> Column<T> getColumnFor(ColumnDefinition<T> columnDefinition);

    <T extends Comparable<T>> Column<T> getColumnForName(String columnName);

    List<Column<?>> getColumns();

    boolean hasColumnFor(ColumnDefinition<?> columnDefinition);

    boolean hasColumnForName(String columnName);

    void setColumn(Column<?> column);

    /**
     * Useful for mapping a record to JSON.
     */
    Map<String, String> toMap();
}

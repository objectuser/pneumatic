package com.surgingsystems.etl.schema;

/**
 * A column value defined by a {@link ColumnDefinition}.
 */
public class Column<T extends Comparable<T>> implements Comparable<Column<T>> {

    private ColumnDefinition<T> columnDefinition;

    private T value;

    public Column() {
    }

    public Column(ColumnDefinition<T> columnDefinition, T value) {
        setColumnDefinition(columnDefinition);
        setValue(value);
    }

    public boolean hasDefinition(ColumnDefinition<T> columnDefinition) {
        return getColumnDefinition().equals(columnDefinition);
    }

    public String getName() {
        return getColumnDefinition().getName();
    }

    public ColumnDefinition<T> getColumnDefinition() {
        return columnDefinition;
    }

    public Class<T> getCoreColumnType() {
        return getColumnDefinition().getCoreType();
    }

    public void setColumnDefinition(ColumnDefinition<T> columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(Column<T> o) {
        return value.compareTo(o.getValue());
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getValue());
    }
}

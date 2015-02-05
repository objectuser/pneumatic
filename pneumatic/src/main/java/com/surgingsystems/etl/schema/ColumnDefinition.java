package com.surgingsystems.etl.schema;

public class ColumnDefinition<T extends Comparable<T>> {

    private String name;

    private ColumnType<T> columnType;

    private int width = 0;

    private boolean nullable = true;

    public ColumnDefinition() {
    }

    public ColumnDefinition(String name, ColumnType<T> columnType) {
        this.name = name;
        this.columnType = columnType;
    }

    public Column<T> applyTo(Column<?> column) {
        return applyToValue(column.getValue());
    }

    public Column<T> applyToValue(Object value) {
        if (columnType.isCompatible(value)) {
            return new Column<T>(this, columnType.convert(value));
        } else {
            return null;
        }
    }

    public Class<T> getCoreType() {
        return getColumnType().getCoreType();
    }

    public boolean accepts(Column<?> column) {
        return acceptsValue(column.getValue());
    }

    public boolean acceptsValue(Object value) {
        return (value != null || nullable) && columnType.isCompatible(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType<T> getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType<T> columnType) {
        this.columnType = columnType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || hashCode() != obj.hashCode() || !getClass().equals(obj.getClass())) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            ColumnDefinition<T> other = (ColumnDefinition<T>) obj;
            return getName().equals(other.getName()) && getColumnType().equals(other.getColumnType());
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), getName(), getColumnType());
    }

}

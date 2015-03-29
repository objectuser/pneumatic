package com.surgingsystems.etl.schema;

import com.surgingsystems.etl.utility.Equality;

public class ColumnDefinition<T extends Comparable<T>> {

    private String name;

    private ColumnType<T> type;

    private int width = 0;

    private boolean nullable = true;

    public ColumnDefinition() {
    }

    public ColumnDefinition(String name, ColumnType<T> type) {
        this.name = name;
        this.type = type;
    }

    public Column<T> applyTo(Column<?> column) {
        return applyToValue(column.getValue());
    }

    public Column<T> applyToValue(Object value) {
        if (type.isCompatible(value)) {
            return new Column<T>(this, type.convert(value));
        } else {
            return null;
        }
    }

    public Class<T> getCoreType() {
        return getType().getCoreType();
    }

    public boolean accepts(Column<?> column) {
        return acceptsValue(column.getValue());
    }

    public boolean acceptsValue(Object value) {
        return (value != null || nullable) && type.isCompatible(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType<T> getType() {
        return type;
    }

    public void setType(ColumnType<T> columnType) {
        this.type = columnType;
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
        ColumnDefinition<T> other = Equality.applicable(this, obj);
        if (other == null) {
            return false;
        } else {
            return getName().equals(other.getName()) && getType().equals(other.getType());
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), getName(), getType());
    }

}

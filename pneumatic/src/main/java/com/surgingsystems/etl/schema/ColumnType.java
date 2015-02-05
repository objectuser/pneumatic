package com.surgingsystems.etl.schema;

public interface ColumnType<T extends Comparable<T>> {
    
    Class<T> getCoreType();
    
    boolean isCompatible(Object value);
    
    T convert(Object value);

    <U extends Comparable<U>> boolean isColumnCompatible(Column<U> column);
}

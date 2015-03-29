package com.surgingsystems.etl.schema;

import java.util.Collection;

import com.surgingsystems.etl.record.Record;

public interface Schema extends Iterable<ColumnDefinition<? extends Comparable<?>>> {
    
    String getName();

    boolean contains(ColumnDefinition<?> columnDefinition);

    ColumnDefinition<? extends Comparable<?>> getColumnForName(String name);

    Collection<ColumnDefinition<? extends Comparable<?>>> getColumns();
    
    Record createEmptyRecord();
}

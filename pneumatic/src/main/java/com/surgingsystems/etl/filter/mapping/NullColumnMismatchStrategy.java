package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;

/**
 * Create a null column.
 */
public class NullColumnMismatchStrategy implements ColumnMismatchStrategy {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean applyTo(Record record, Object inputColumn, ColumnDefinition<?> toColumnDefinition) {
        record.setColumn(new Column(toColumnDefinition, null));
        return false;
    }
}

package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.ColumnDefinition;

public interface ColumnMismatchStrategy {

    /**
     * Apply the strategy to the record.
     * 
     * @return true if the mismatch should result in a reject.
     */
    boolean applyTo(Record record, Object value, ColumnDefinition<?> toColumnDefinition);
}

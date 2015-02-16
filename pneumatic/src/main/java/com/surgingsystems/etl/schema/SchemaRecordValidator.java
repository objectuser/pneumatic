package com.surgingsystems.etl.schema;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;

public class SchemaRecordValidator implements RecordValidator {

    private Schema schema;

    public SchemaRecordValidator(Schema schema) {
        this.schema = schema;
    }

    /**
     * Is the record compatible with this schema?
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean accepts(Record record) {
        boolean result = true;
        for (ColumnDefinition<?> columnDefinition : schema) {
            Column column = record.getColumnFor(columnDefinition);
            if (column == null) {
                result = false;
                break;
            } else {
                if (column.getValue() != null && !columnDefinition.accepts(column)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}

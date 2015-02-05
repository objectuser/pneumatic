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
    @Override
    public boolean accepts(Record record) {
        boolean result = true;
        for (ColumnDefinition<? extends Comparable<?>> columnDefinition : schema.getColumnDefinitions()) {
            Column<?> column = record.getColumnFor((ColumnDefinition<?>) columnDefinition);
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

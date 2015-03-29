package com.surgingsystems.etl.record;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class DataRecord extends RecordWithColumnDefaults {

    public DataRecord() {
    }

    /**
     * Create an empty record with columns from the schema.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DataRecord(Schema schema) {
        for (ColumnDefinition<?> columnDefinition : schema) {
            addColumn(new Column(columnDefinition, null));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DataRecord(Schema schema, Object... values) {
        if (schema.getColumns().size() != values.length) {
            throw new IllegalArgumentException(String.format(
                    "Record has %d columns, %d values provided - there must be one value for each column definition",
                    schema.getColumns().size(), values.length));
        }

        int i = 0;
        for (ColumnDefinition<?> columnDefinition : schema) {
            Comparable<?> value = null;
            if (values.length > i) {
                value = (Comparable<?>) values[i++];
            }
            addColumn(new Column(columnDefinition, value));
        }
    }
}

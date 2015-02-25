package com.surgingsystems.etl.record;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class XmlRecord extends RecordWithColumnDefaults {

    public XmlRecord() {
    }

    /**
     * Create an empty record with columns from the schema.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public XmlRecord(Schema schema) {
        for (ColumnDefinition<?> columnDefinition : schema) {
            addColumn(new Column(columnDefinition, null));
        }
    }
}

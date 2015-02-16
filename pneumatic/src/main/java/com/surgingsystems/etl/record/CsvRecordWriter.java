package com.surgingsystems.etl.record;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class CsvRecordWriter implements RecordWriter {

    @SuppressWarnings("rawtypes")
    @Override
    public String write(Record record, Schema schema) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int columnCount = schema.getColumnDefinitions().size();
        for (ColumnDefinition<?> columnDefinition : schema) {
            Column column = record.getColumnFor(columnDefinition);
            Object value = column.getValue();
            if (value != null) {
                result.append('"').append(column.getValue()).append('"');
            }

            if (i < (columnCount - 1)) {
                result.append(",");
            }
            ++i;
        }
        return result.toString();
    }
}

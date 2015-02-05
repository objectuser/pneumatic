package com.surgingsystems.etl.record;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class CsvRecordParser implements RecordParser<String> {

    private static Logger logger = LogManager.getFormatterLogger(CsvRecordParser.class);

    @Override
    public Record parse(String line, Schema schema) {
        DataRecord result = new DataRecord();
        String[] values = line.split(",");
        if (values.length != schema.getColumnDefinitions().size()) {
            logger.warn("Record (%s...) has a different number of values (%d) than schema (%s) (%d)", line.substring(0,
                    Math.min(line.length(), 100)), values.length, schema.getName(), schema.getColumnDefinitions()
                    .size());
            return null;
        }

        int i = 0;
        for (ColumnDefinition<? extends Comparable<?>> columnDefinition : schema) {
            Column<?> column = columnDefinition.applyToValue(values[i]);
            if (column == null) {
                logger.warn("Column definition (%s) of schema (%s) incompatible with value (%s)",
                        columnDefinition.getName(), schema.getName(), values[i]);
                return null;
            } else {
                result.addColumn(column);
            }

            ++i;
        }

        return result;
    }

}

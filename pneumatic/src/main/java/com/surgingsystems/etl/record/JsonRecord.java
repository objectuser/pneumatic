package com.surgingsystems.etl.record;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class JsonRecord extends RecordWithColumnDefaults {

    public JsonRecord() {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JsonRecord(Schema schema, Object... values) {
        if (schema.getColumnDefinitions().size() != values.length) {
            throw new IllegalArgumentException(String.format(
                    "Record has %d columns, %d values provided - there must be one value for each column definition",
                    schema.getColumnDefinitions().size(), values.length));
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

    public static JsonRecord create(Schema schema, String json) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();

        map = mapper.readValue(json, new TypeReference<HashMap<String, String>>() {
        });

        return mapRecord(schema, map);
    }

    private static JsonRecord mapRecord(Schema schema, Map<String, String> input) {
        JsonRecord record = new JsonRecord();
        for (ColumnDefinition<?> columnDefinition : schema) {
            String stringValue = input.get(columnDefinition.getName());
            Column<?> column = columnDefinition.applyToValue(stringValue);
            record.setColumn(column);
        }
        return record;
    }

}

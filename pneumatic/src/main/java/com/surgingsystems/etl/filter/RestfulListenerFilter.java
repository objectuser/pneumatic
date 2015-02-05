package com.surgingsystems.etl.filter;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class RestfulListenerFilter implements Filter {

    private String name = "RestfulFilter";
    
    private String path;

    private Pipe output;

    private Schema schema;

    public String filter(String path, String json) {

        try {
            Map<String, String> map = new HashMap<String, String>();
            ObjectMapper mapper = new ObjectMapper();

            map = mapper.readValue(json, new TypeReference<HashMap<String, String>>() {
            });

            Record record = createRecord(map);
            output.put(record);

        } catch (Exception exception) {
            throw new RuntimeException("Unable to handle request");
        }

        return "";
    }

    private Record createRecord(Map<String, String> input) {
        DataRecord xmlRecord = new DataRecord();
        for (ColumnDefinition<?> columnDefinition : schema) {
            String stringValue = input.get(columnDefinition.getName());
            Column<?> column = columnDefinition.applyToValue(stringValue);
            xmlRecord.addColumn(column);
        }
        return xmlRecord;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

}

package com.surgingsystems.etl.filter.transformer;

import java.util.ArrayList;
import java.util.List;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.schema.Schema;

public class TransformerFilterOutputConfiguration {

    private String name;

    private String recordName;

    private Schema schema;

    private Pipe output;

    private String outputCondition = "true";

    private List<String> expressions = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schema getSchema() {
        return schema;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Pipe getPipe() {
        return output;
    }

    public void setOutput(Pipe pipe) {
        this.output = pipe;
    }

    public String getOutputCondition() {
        return outputCondition;
    }

    public void setOutputCondition(String outputCondition) {
        this.outputCondition = outputCondition;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getName());
    }
}

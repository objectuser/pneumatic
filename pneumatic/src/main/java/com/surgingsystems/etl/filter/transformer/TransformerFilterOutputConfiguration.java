package com.surgingsystems.etl.filter.transformer;

import java.util.ArrayList;
import java.util.List;

import com.surgingsystems.etl.schema.Schema;

public class TransformerFilterOutputConfiguration extends ConditionalOutputConfiguration {

    private String recordName;

    private Schema outputSchema;

    private List<String> expressions = new ArrayList<String>();

    public TransformerFilterOutputConfiguration() {
        setConditionExpression("true");
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setOutputSchema(Schema schema) {
        this.outputSchema = schema;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }
}

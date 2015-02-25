package com.surgingsystems.etl.filter;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.JsonRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

public class RestfulListenerFilter implements Filter {

    private static Logger logger = LogManager.getFormatterLogger(RestfulListenerFilter.class);

    private String name;

    private String path;

    private Pipe output;

    private Schema outputSchema;

    @PostConstruct
    public void validate() {
        Assert.notNull(getPath(), "The path is required");
        Assert.notNull(getOutput(), "The output is required");
        Assert.notNull(getOutputSchema(), "The output schema is required");
    }

    public void filter(String path, String json) {
        try {
            logger.trace("Filter (%s) recevied message", getName());
            Record record = JsonRecord.create(outputSchema, json);
            output.put(record);
        } catch (Exception exception) {
            throw new RuntimeException("Unable to handle request");
        }
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

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema schema) {
        this.outputSchema = schema;
    }
}

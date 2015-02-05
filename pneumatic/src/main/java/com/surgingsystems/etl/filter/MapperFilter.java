package com.surgingsystems.etl.filter;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.Mapping;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.schema.AcceptingRecordValidator;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.SchemaRecordValidator;

/**
 * This filter provides the following features:
 * <ul>
 * <li>A single input.</li>
 * <li>A single output.</li>
 * <li>An optional schema defining the input.</li>
 * <li>A required schema defining the output.</li>
 * <li>A {@link Mapping} that provides the translation from the input columns to
 * the output columns. The output schema's column definition and column order
 * are applied to the input record.</li>
 * <li>Values on on the input record are converted to the data types of values
 * on the output record if the types support the conversion and the values are
 * convertable.</li>
 * </ul>
 */
public class MapperFilter extends SingleInputFilter implements InputFilter, OutputFilter {

    private static Logger logger = LogManager.getFormatterLogger(MapperFilter.class);

    private Pipe output;

    private Mapping mapping = new Mapping();

    private Schema inputSchema;

    private Schema outputSchema;

    private RecordValidator inputRecordValidator;

    private RecordValidator outputRecordValidator;

    private RejectRecordStrategy rejectRecordStrategy = new LogRejectRecordStrategy();

    public MapperFilter() {
    }

    public MapperFilter(String name) {
        setName(name);
    }

    @PostConstruct
    public void validate() {

        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
        Assert.notNull(output, "The output pipe is required");
        Assert.notNull(outputSchema, "The output schema is required");

        mapping.setOutputSchema(outputSchema);
        if (inputSchema != null) {
            mapping.validate(inputSchema);
        }
    }

    @PostConstruct
    public void createRecordValidator() {
        if (inputSchema != null) {
            logger.trace("Input schema supplied - input records will be validated against the schema");
            inputRecordValidator = new SchemaRecordValidator(inputSchema);
        } else {
            logger.trace("No input schema supplied - input records will not be validated");
            inputRecordValidator = new AcceptingRecordValidator();
        }

        outputRecordValidator = new SchemaRecordValidator(outputSchema);
    }

    @Override
    protected void preProcess() {
    }

    @Override
    protected void process(Record inputRecord) throws Exception {

        if (!inputRecordValidator.accepts(inputRecord)) {
            rejectRecordStrategy.rejected(inputRecord);
        } else {

            logRecord(inputRecord);
            recordProcessed();

            Record outputRecord = mapping.map(inputRecord);
            if (outputRecord != null && outputRecordValidator.accepts(outputRecord)) {
                output.put(outputRecord);
            } else {
                rejectRecordStrategy.rejected(inputRecord);
            }
        }
    }

    @Override
    protected void postProcess() {
        output.closedForInput();
        logSummary();
    }

    @Override
    public void addOutput(Pipe pipe) {
        output = pipe;
    }

    @Override
    public void addInput(Pipe pipe) {
        setInput(pipe);
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }
}

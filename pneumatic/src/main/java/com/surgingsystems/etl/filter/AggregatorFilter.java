package com.surgingsystems.etl.filter;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.filter.function.Function;
import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.schema.AcceptingRecordValidator;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.SchemaRecordValidator;

/**
 * Calculates summary information based on the input. Use one of the supplied
 * {@link Function} implementations or write your own. Outputs flow after the
 * input has been completed. Aggregators have the following features:
 * <ul>
 * <li>A single input.</li>
 * <li>A optional schema for the input.</li>
 * <li>An output that contains the result of the aggregator function for all
 * input.</li>
 * <li>A schema for the The schema is used to create the output record sent on
 * the aggregator output.</li>
 * <li>A function for the aggregation calculation.
 * <ul>
 * <li>If the function includes an input column definition, that definition must
 * match a column on the input.</li>
 * <li>Functions include an output column definition. This column must match a
 * column on the aggregator output.</li>
 * </ul>
 * </ul>
 */
public class AggregatorFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(AggregatorFilter.class);

    private Schema inputSchema;

    private Pipe output;

    private Schema outputSchema;

    private RecordValidator inputRecordValidator;

    private RejectRecordStrategy rejectRecordStrategy = new LogRejectRecordStrategy();

    private Function<?> function;

    public AggregatorFilter() {
    }

    public AggregatorFilter(String name, Function<?> function) {
        setName(name);
        this.function = function;
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input is required");
        Assert.notNull(output, "The output is required");
        Assert.notNull(function, "The function is required");

        if (inputSchema != null) {
            logger.debug("The input schema has been specified. Validating the input schema against the supplied function.");
            Assert.isTrue(function.containsRequiredInput(inputSchema), String.format(
                    "Aggregator (%s): The input schema must have a column matching the input column of the function",
                    getName()));

            inputRecordValidator = new SchemaRecordValidator(inputSchema);

        } else {
            inputRecordValidator = new AcceptingRecordValidator();
        }

        if (outputSchema != null) {
            logger.debug("The output schema has been specified. Validating the output schema against the supplied function.");
            Assert.isTrue(
                    function.containsRequiredOutput(outputSchema),
                    String.format(
                            "Aggregator (%s): The aggregator schema must have a column matching the output column of the function",
                            getName()));
        }
    }

    @Override
    protected void process(Record record) throws Exception {

        recordProcessed();

        if (!inputRecordValidator.accepts(record)) {
            rejectRecordStrategy.rejected(record);
        } else {

            logRecord(record);

            function.apply(record);
        }
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    protected void postProcess() {

        Column aggregation = function.getResult();
        DataRecord aggregationRecord = null;
        if (outputSchema != null) {
            aggregationRecord = new DataRecord(outputSchema);
        } else {
            aggregationRecord = new DataRecord();
        }

        aggregationRecord.addColumn(aggregation);

        output.put(aggregationRecord);
        output.closedForInput();

        rejectRecordStrategy.close();

        logSummary();
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Schema inputSchema) {
        this.inputSchema = inputSchema;
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

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> Function<T> getFunction() {
        return (Function<T>) function;
    }

    public void setFunction(Function<?> function) {
        this.function = function;
    }

    public RejectRecordStrategy getRejectRecordStrategy() {
        return rejectRecordStrategy;
    }

    public void setRejectRecordStrategy(RejectRecordStrategy rejectRecordStrategy) {
        this.rejectRecordStrategy = rejectRecordStrategy;
    }
}

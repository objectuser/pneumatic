package com.surgingsystems.etl.filter;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.ArrayRecordAggregator;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordAggregator;
import com.surgingsystems.etl.record.RecordCapture;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.schema.AcceptingRecordValidator;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.SchemaRecordValidator;

/**
 * Supports writing a flat file to the file system. File writer filters have the
 * following features:
 * <ul>
 * <li>A file resource for containing the output data.</li>
 * <li>A single input.</li>
 * <li>A single schema for the input.</li>
 * </ul>
 */
public class FileWriterFilter extends SingleInputFilter implements InputFilter {

    private static Logger logger = LogManager.getFormatterLogger(FileWriterFilter.class);

    private Resource fileResource;

    private Schema inputSchema;

    private RecordAggregator recordAggregator = new ArrayRecordAggregator();

    private FlatFileItemWriter<String[]> itemWriter = new FlatFileItemWriter<String[]>();

    private RecordValidator recordValidator;

    public FileWriterFilter() {
    }

    public FileWriterFilter(String name, Resource fileResource) {
        setName(name);
        setFileResource(fileResource);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(inputSchema, "The output schema is required");
        Assert.notNull(fileResource, "The file resource is required");
    }

    @PostConstruct
    public void createRecordValidator() {
        if (inputSchema != null) {
            recordValidator = new SchemaRecordValidator(inputSchema);
        } else {
            recordValidator = new AcceptingRecordValidator();
        }
    }

    @Override
    protected void preProcess() throws Exception {
        itemWriter.setResource(fileResource);
        itemWriter.open(new ExecutionContext());
        itemWriter.setLineAggregator(new DelimitedLineAggregator<String[]>());
    }

    @Override
    protected void process(Record record) throws Exception {

        if (!recordValidator.accepts(record)) {
            logger.warn("Schema (%s) is incompatible with record [%s...]", inputSchema.getName(),
                    RecordCapture.capture(record));
        } else {

            recordProcessed();
            logRecord(record);

            String[] values = recordAggregator.aggregate(record);
            itemWriter.write(Arrays.<String[]> asList(values));
        }
    }

    @Override
    protected void postProcess() throws Exception {
        if (itemWriter != null) {
            itemWriter.close();
        }

        logSummary();
    }

    @Override
    public void addInput(Pipe pipe) {
        setInput(pipe);
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Schema schema) {
        this.inputSchema = schema;
    }

    public Resource getFileResource() {
        return fileResource;
    }

    public void setFileResource(Resource fileResource) {
        this.fileResource = fileResource;
    }
}

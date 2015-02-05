package com.surgingsystems.etl.filter;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.ArrayRecordParser;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordParser;
import com.surgingsystems.etl.schema.Schema;

/**
 * Supports reading from a flat file on the file system. File reader filters
 * have the following features:
 * <ul>
 * <li>A file resource containing the data to be read.</li>
 * <li>A single output.</li>
 * <li>A single schema for the output.</li>
 * <li>A boolean property that indicates the first line of the file contains
 * header information (and should be skipped). Note that the header information
 * is never used.</li>
 * </ul>
 */
public class FileReaderFilter extends GuardedFilter implements OutputFilter {

    private static Logger logger = LogManager.getFormatterLogger(FileReaderFilter.class);

    private Pipe output;

    private Schema schema;

    private RecordParser<String[]> recordParser = new ArrayRecordParser();

    private FlatFileRecordReader itemReader;

    public FileReaderFilter() {
    }

    public FileReaderFilter(String name, FlatFileRecordReader flatFileRecordReader, Schema schema) {
        setName(name);
        setItemReader(flatFileRecordReader);
        setSchema(schema);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(output, "The output pipe is required");
        Assert.notNull(schema, "The output schema is required");
        Assert.notNull(itemReader, "The file resource is required");
    }

    @Override
    public void addOutput(Pipe pipe) {
        setOutput(pipe);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public void setRecordParser(RecordParser<String[]> recordParser) {
        this.recordParser = recordParser;
    }

    @Override
    protected void filter() throws Exception {

        itemReader.open(new ExecutionContext());

        try {
            String[] input = null;
            while ((input = itemReader.read()) != null) {

                if (logger.isTraceEnabled()) {
                    String line = Arrays.toString(input);
                    logger.trace("Reading from [%s]: [%s...]", itemReader.getResource().getFilename(),
                            line.substring(0, Math.min(line.length(), 100)));
                }

                Record record = recordParser.parse(input, schema);
                if (record == null) {
                    logger.warn("Parser returned null record when parsing according to schema (%s)", schema.getName());
                } else {
                    recordProcessed();
                    logRecord(record);
                    output.put(record);
                }
            }

            output.closedForInput();

            logSummary();

        } finally {
            itemReader.close();
        }
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

    public FlatFileRecordReader getItemReader() {
        return itemReader;
    }

    public void setItemReader(FlatFileRecordReader itemReader) {
        this.itemReader = itemReader;
    }
}

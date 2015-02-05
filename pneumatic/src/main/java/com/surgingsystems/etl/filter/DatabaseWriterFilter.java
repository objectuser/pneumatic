package com.surgingsystems.etl.filter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.filter.database.DatabaseWriteStrategy;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordCapture;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.schema.AcceptingRecordValidator;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.SchemaRecordValidator;

/**
 * Supports reading from a database using a select statement and applied
 * arguments. Database reader filters have the following features:
 * <ul>
 * <li>A single select statement for reading data, with any number of arguments.
 * </li>
 * <li>A data source that supplies the connection to the target database.</li>
 * <li>A single output.</li>
 * <li>A single schema for the output.</li>
 * </ul>
 */
public class DatabaseWriterFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(DatabaseWriterFilter.class);

    private Schema inputSchema;

    private DataSource dataSource;

    private DatabaseWriteStrategy writeStrategy;

    private RecordValidator recordValidator;

    public DatabaseWriterFilter() {
    }

    public DatabaseWriterFilter(String name, DataSource dataSource) {
        setName(name);
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void constructSql() {
        writeStrategy.initialize(dataSource, inputSchema);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
        Assert.notNull(dataSource, "The data source is required");
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
    protected void process(Record record) throws Exception {
        if (recordValidator.accepts(record)) {
            writeStrategy.write(record);
        } else {
            logger.warn("%s: Record does not match schema: [%s...]", getName(), RecordCapture.capture(record));
        }
    }

    @Override
    protected void postProcess() throws Exception {
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    public DatabaseWriteStrategy getWriteStrategy() {
        return writeStrategy;
    }

    public void setWriteStrategy(DatabaseWriteStrategy writeStrategy) {
        this.writeStrategy = writeStrategy;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

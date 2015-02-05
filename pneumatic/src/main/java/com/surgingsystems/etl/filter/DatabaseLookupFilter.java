package com.surgingsystems.etl.filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.Mapping;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.record.ResultSetRecord;
import com.surgingsystems.etl.schema.ColumnDefinition;
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
public class DatabaseLookupFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(DatabaseLookupFilter.class);

    private Pipe output;

    private Schema inputSchema;

    private Schema outputSchema;

    private Schema criteriaSchema;
    
    private Mapping mapping;

    private RecordValidator recordValidator;
    
    private RejectRecordStrategy rejectRecordStrategy = new LogRejectRecordStrategy();

    private String sqlSelect;

    private JdbcTemplate jdbcTemplate;

    public DatabaseLookupFilter() {
    }

    public DatabaseLookupFilter(String name, DataSource dataSource) {
        setName(name);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
        Assert.notNull(output, "The output pipe is required");
        Assert.notNull(outputSchema, "The output schema is required");
        Assert.notNull(sqlSelect, "The select statement is required");
        Assert.notNull(jdbcTemplate, "The data source is required");

        recordValidator = new SchemaRecordValidator(outputSchema);
        
        mapping = new Mapping(outputSchema);
    }

    @Override
    protected void process(Record inputRecord) {

        List<Object> arguments = new ArrayList<Object>();
        for (ColumnDefinition<?> columnDefinition : criteriaSchema) {
            Object value = inputRecord.getValueFor(columnDefinition);
            arguments.add(value);
        }

        Object[] args = arguments.toArray(new Object[] {});

        jdbcTemplate.query(sqlSelect, args, new RowMapper<Record>() {
            
            private ResultSetRecord resultSetRecord;

            @Override
            public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (resultSetRecord == null) {
                    resultSetRecord = new ResultSetRecord(rs);
                }
                
                Record outputRecord = mapping.map(resultSetRecord);
                if (outputRecord != null && recordValidator.accepts(outputRecord)) {
                    output.put(outputRecord);
                } else {
                    logger.trace("Record (%d) does not comply with schema (%s)", rowNum, outputSchema.getName());
                    rejectRecordStrategy.rejected(resultSetRecord);
                }
                
                return outputRecord;
            }
        });
    }

    @Override
    protected void postProcess() throws Exception {
        output.closedForInput();
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
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

    public Schema getCriteriaSchema() {
        return criteriaSchema;
    }

    public void setCriteriaSchema(Schema criteriaSchema) {
        this.criteriaSchema = criteriaSchema;
    }

    public String getSqlSelect() {
        return sqlSelect;
    }

    public void setSqlSelect(String sqlSelect) {
        this.sqlSelect = sqlSelect;
    }
}

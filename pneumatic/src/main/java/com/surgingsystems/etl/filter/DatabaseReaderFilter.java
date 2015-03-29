package com.surgingsystems.etl.filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.surgingsystems.etl.context.EtlContextHolder;
import com.surgingsystems.etl.filter.expression.EtlExpressionHelper;
import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.Mapping;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.record.ResultSetRecord;
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
public class DatabaseReaderFilter extends GuardedFilter {

    private static Logger logger = LogManager.getFormatterLogger(DatabaseReaderFilter.class);

    @Autowired
    private EtlContextHolder etlContextHolder;

    private String sql;

    private List<String> parameters = new ArrayList<String>();

    private Pipe output;

    private Schema outputSchema;

    private JdbcTemplate jdbcTemplate;

    private Mapping mapping = new Mapping();

    private RecordValidator recordValidator;

    private RejectRecordStrategy rejectRecordStrategy;

    private EtlExpressionHelper expressionHelper = new EtlExpressionHelper();

    public DatabaseReaderFilter() {
    }

    public DatabaseReaderFilter(String name, DataSource dataSource) {
        setName(name);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(output, "The output pipe is required");
        Assert.notNull(outputSchema, "The output schema is required");
        Assert.notNull(sql, "The select statement is required");
        Assert.notNull(jdbcTemplate, "The data source is required");

        mapping.setOutputSchema(outputSchema);
        mapping.validate(outputSchema);

        recordValidator = new SchemaRecordValidator(outputSchema);

        setupExpressions();
    }

    @PostConstruct
    public void setupRejectionStrategy() {
        if (rejectRecordStrategy == null) {
            rejectRecordStrategy = new LogRejectRecordStrategy(getName());
        }
    }

    @Override
    protected void filter() {

        List<Object> arguments = new ArrayList<Object>();
        for (String parameter : parameters) {
            Object value = expressionHelper.evaluate(parameter);
            arguments.add(value);
        }

        Object[] args = arguments.toArray(new Object[] {});

        jdbcTemplate.query(sql, args, new RowMapper<Record>() {

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
                    logger.warn("Record (%d) does not comply with schema (%s)", rowNum, outputSchema.getName());
                    rejectRecordStrategy.rejected(resultSetRecord);
                }

                return outputRecord;
            }
        });

        output.closedForInput();
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sqlSelect) {
        this.sql = sqlSelect;
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema schema) {
        this.outputSchema = schema;
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> args) {
        this.parameters = args;
    }

    private void setupExpressions() {
        expressionHelper.applyContext(etlContextHolder);
    }
}

package com.surgingsystems.etl.filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.surgingsystems.etl.filter.expression.EtlExpressionHelper;
import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.Mapping;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.record.ResultSetRecord;
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
public class DatabaseLookupFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(DatabaseLookupFilter.class);

    private Pipe output;

    private Schema inputSchema;

    private Schema outputSchema;

    private Mapping mapping;

    private RecordValidator inputRecordValidator;

    private RecordValidator outputRecordValidator;

    private RejectRecordStrategy rejectRecordStrategy;

    private String sql;

    private List<String> parameters;

    private List<Expression> parameterExpressions = new ArrayList<Expression>();

    private EtlExpressionHelper expressionHelper = new EtlExpressionHelper();

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
        Assert.notNull(sql, "The select statement is required");
        Assert.notNull(jdbcTemplate, "The data source is required");

        mapping = new Mapping(outputSchema);

        if (inputSchema == null) {
            inputRecordValidator = new AcceptingRecordValidator();
        } else {
            inputRecordValidator = new SchemaRecordValidator(inputSchema);
        }

        outputRecordValidator = new SchemaRecordValidator(outputSchema);

        setupExpressions();
    }

    @PostConstruct
    public void setupRejectionStrategy() {
        if (rejectRecordStrategy == null) {
            rejectRecordStrategy = new LogRejectRecordStrategy(getName());
        }
    }

    @Override
    protected void processRecord(Record inputRecord) {

        if (!inputRecordValidator.accepts(inputRecord)) {
            rejectRecordStrategy.rejected(inputRecord);
        } else {

            expressionHelper.setVariable("inputRecord", inputRecord);

            List<Object> arguments = new ArrayList<Object>();
            for (Expression expression : parameterExpressions) {
                Object value = expressionHelper.getValue(expression);
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

                    Record outputRecord = mapping.map(inputRecord, resultSetRecord);
                    if (outputRecord != null && outputRecordValidator.accepts(outputRecord)) {
                        output.put(outputRecord);
                    } else {
                        logger.trace("Record (%d) does not comply with schema (%s)", rowNum, outputSchema.getName());
                        rejectRecordStrategy.rejected(resultSetRecord);
                    }

                    return outputRecord;
                }
            });
        }
    }

    @Override
    protected void postProcess() throws Exception {
    }

    @Override
    protected void cleanUp() throws Exception {
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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    private void setupExpressions() {
        for (String parameter : parameters) {
            Expression expression = expressionHelper.parse(parameter);
            parameterExpressions.add(expression);
        }
    }
}

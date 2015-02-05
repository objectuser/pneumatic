package com.surgingsystems.etl.filter.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.surgingsystems.etl.dsl.filter.RecordPropertyAccessor;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

public class ConfigurableDatabaseWriteStrategy implements DatabaseWriteStrategy {

    private String sql;

    private List<String> parameters;

    private JdbcTemplate jdbcTemplate;

    private ExpressionParser expressionParser;

    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private RecordPropertyAccessor recordPropertyAccessor = new RecordPropertyAccessor();

    @Override
    public void initialize(DataSource dataSource, Schema schema) {

        setupParser();

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(Record record) {

        evaluationContext.setVariable("inputRecord", record);

        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);

                int i = 0;
                for (String parameter : parameters) {
                    Object value = expressionParser.parseExpression(parameter).getValue(evaluationContext);
                    statement.setObject(++i, value);
                }

                return statement;
            }
        });
    }

    private void setupParser() {
        SpelParserConfiguration spelParserConfiguration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this
                .getClass().getClassLoader());
        expressionParser = new SpelExpressionParser(spelParserConfiguration);
        evaluationContext.addPropertyAccessor(recordPropertyAccessor);
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
}

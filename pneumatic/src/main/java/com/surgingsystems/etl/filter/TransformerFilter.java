package com.surgingsystems.etl.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import com.surgingsystems.etl.dsl.filter.RecordPropertyAccessor;
import com.surgingsystems.etl.filter.transformer.TransformerFilterOutputConfiguration;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

/**
 * Transformers use SpEL expressions to create a programmable filter.
 */
public class TransformerFilter extends SingleInputFilter implements InputFilter {

    private List<TransformerFilterOutputConfiguration> outputConfigurations = new ArrayList<TransformerFilterOutputConfiguration>();

    private Map<String, Object> variables = new TreeMap<String, Object>();

    private List<String> expressions = new ArrayList<String>();

    private ExpressionParser expressionParser;

    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private RecordPropertyAccessor recordPropertyAccessor = new RecordPropertyAccessor();

    private List<Expression> parsedExpressions = new ArrayList<Expression>();

    private Map<TransformerFilterOutputConfiguration, Expression> outputConditions = new HashMap<TransformerFilterOutputConfiguration, Expression>();

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
    }

    @PostConstruct
    public void setupExpressionParser() {
        setupParser();
        initializeVariables();
        initializeExpressions();
    }

    @Override
    protected void process(Record inputRecord) throws Exception {

        // for each record, we setup the input record for the expressions
        evaluationContext.setVariable("inputRecord", inputRecord);

        Map<TransformerFilterOutputConfiguration, Record> outputRecords = new HashMap<TransformerFilterOutputConfiguration, Record>();

        // We setup all the variables before running any expression to make the
        // expressions behave as expected.
        for (TransformerFilterOutputConfiguration config : outputConfigurations) {
            Schema schema = config.getSchema();
            DataRecord dataRecord = new DataRecord(schema);
            outputRecords.put(config, dataRecord);

            evaluationContext.setVariable(config.getRecordName(), dataRecord);
        }

        for (Expression expression : parsedExpressions) {
            expression.getValue(evaluationContext);
        }

        for (Map.Entry<TransformerFilterOutputConfiguration, Record> entry : outputRecords.entrySet()) {
            TransformerFilterOutputConfiguration config = entry.getKey();
            Expression outputConditionExpression = outputConditions.get(entry.getKey());
            Boolean outputCondition = (Boolean) outputConditionExpression.getValue(evaluationContext);
            if (outputCondition) {
                Pipe pipe = config.getPipe();
                Record record = entry.getValue();
                pipe.put(record);
            }
        }
    }

    @Override
    protected void postProcess() throws Exception {
        for (TransformerFilterOutputConfiguration config : outputConfigurations) {
            Pipe pipe = config.getPipe();
            pipe.closedForInput();
        }
    }

    public void setOutputConfigurations(List<TransformerFilterOutputConfiguration> outputs) {
        this.outputConfigurations.addAll(outputs);
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables.putAll(variables);
    }

    public void setExpressions(List<String> expressions) {
        this.expressions.addAll(expressions);
    }

    public List<TransformerFilterOutputConfiguration> getOutputConfigurations() {
        return outputConfigurations;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    private void setupParser() {
        SpelParserConfiguration spelParserConfiguration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this
                .getClass().getClassLoader());
        expressionParser = new SpelExpressionParser(spelParserConfiguration);
        evaluationContext.addPropertyAccessor(recordPropertyAccessor);
    }

    private void initializeVariables() {
        // input is available for the expressions
        evaluationContext.setVariable("input", getInput());

        for (TransformerFilterOutputConfiguration config : outputConfigurations) {
            evaluationContext.setVariable(config.getName(), config.getPipe());
        }

        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            String initializationExpression = (String) variable.getValue();
            Object value = null;
            if (!StringUtils.isEmpty(initializationExpression)) {
                value = expressionParser.parseExpression(initializationExpression).getValue(evaluationContext);
            }
            evaluationContext.setVariable(variable.getKey(), value);
        }
    }

    private void initializeExpressions() {
        for (String expression : expressions) {
            parsedExpressions.add(expressionParser.parseExpression(expression));
        }

        for (TransformerFilterOutputConfiguration config : outputConfigurations) {
            for (String expression : config.getExpressions()) {
                parsedExpressions.add(expressionParser.parseExpression(expression));
            }

            outputConditions.put(config, expressionParser.parseExpression(config.getOutputCondition()));
        }
    }

}

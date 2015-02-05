package com.surgingsystems.etl.filter.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.surgingsystems.etl.dsl.filter.RecordPropertyAccessor;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class ExpressionFunction<T extends Comparable<T>> implements Function<T> {
    
    private Schema outputSchema;
    
    private Record outputRecord;
    
    private ColumnDefinition<T> outputColumnDefinition;

    private Map<String, Object> variables = new TreeMap<String, Object>();

    private List<String> expressions = new ArrayList<String>();

    private ExpressionParser expressionParser;

    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private RecordPropertyAccessor recordPropertyAccessor = new RecordPropertyAccessor();

    @PostConstruct
    public void setupExpressionParser() {
        setupParser();
        initializeVariables();
        
        outputRecord = new DataRecord(outputSchema);
    }

    @Override
    public void apply(Record record) {

        evaluationContext.setVariable("input", record);
        evaluationContext.setVariable("output", outputRecord);

        for (String expression : expressions) {
            expressionParser.parseExpression(expression).getValue(evaluationContext);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Column<T> getResult() {
        return (Column<T>) outputRecord.getColumns().get(0);
    }
    
    private void setupParser() {
        SpelParserConfiguration spelParserConfiguration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this
                .getClass().getClassLoader());
        expressionParser = new SpelExpressionParser(spelParserConfiguration);
        evaluationContext.addPropertyAccessor(recordPropertyAccessor);
    }

    private void initializeVariables() {
        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            String initializationExpression = (String) variable.getValue();
            Object value = null;
            if (!StringUtils.isEmpty(initializationExpression)) {
                value = expressionParser.parseExpression(initializationExpression).getValue(evaluationContext);
            }
            evaluationContext.setVariable(variable.getKey(), value);
        }
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    public Record getOutputRecord() {
        return outputRecord;
    }

    public void setOutputRecord(Record outputRecord) {
        this.outputRecord = outputRecord;
    }

    @Override
    public ColumnDefinition<T> getOutputColumnDefinition() {
        return outputColumnDefinition;
    }

    public void setOutputColumnDefinition(ColumnDefinition<T> outputColumnDefinition) {
        this.outputColumnDefinition = outputColumnDefinition;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }

    @Override
    public boolean containsRequiredInput(Schema inputSchema) {
        return true; //inputSchema.contains(inputColumnDefinition);
    }

    @Override
    public boolean containsRequiredOutput(Schema aggregatorOutputSchema) {
        return aggregatorOutputSchema.contains(outputColumnDefinition);
    }
}

package com.surgingsystems.etl.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import com.surgingsystems.etl.dsl.filter.RecordPropertyAccessor;
import com.surgingsystems.etl.filter.transformer.OutputCondition;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class SplitterFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(SplitterFilter.class);

    private List<OutputCondition> outputConditions = new ArrayList<>();

    private ExpressionParser expressionParser;

    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private RecordPropertyAccessor recordPropertyAccessor = new RecordPropertyAccessor();

    private Map<OutputCondition, Expression> expressions = new HashMap<>();

    @PostConstruct
    public void validate() {
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
        Assert.isTrue(getOutputConditions().size() > 0, "There must be at least one output defined");
    }

    @PostConstruct
    public void setupExpressionParser() {
        setupParser();
        initializeVariables();
        initializeExpressions();
    }

    @Override
    protected void process(Record inputRecord) throws Exception {

        evaluationContext.setVariable("inputRecord", inputRecord);

        for (OutputCondition outputCondition : outputConditions) {
            if (logger.isTraceEnabled()) {
                logger.trace("Evaluating condition: %s", outputCondition.getConditionExpression());
            }

            Expression expression = expressions.get(outputCondition);
            Boolean outputValue = (Boolean) expression.getValue(evaluationContext);
            if (outputValue) {
                Pipe output = outputCondition.getOutput();
                output.put(inputRecord);
            }
        }
    }

    @Override
    protected void postProcess() throws Exception {
        for (OutputCondition outputCondition : outputConditions) {
            outputCondition.close();
        }
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
    }

    private void initializeExpressions() {
        for (OutputCondition outputCondition : outputConditions) {
            expressions.put(outputCondition, expressionParser.parseExpression(outputCondition.getConditionExpression()));
        }
    }

    public List<OutputCondition> getOutputConditions() {
        return outputConditions;
    }

    public void setOutputConditions(List<OutputCondition> outputConditions) {
        this.outputConditions = outputConditions;
    }

}

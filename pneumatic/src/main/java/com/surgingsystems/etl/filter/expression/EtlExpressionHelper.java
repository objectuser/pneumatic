package com.surgingsystems.etl.filter.expression;

import java.util.ArrayList;
import java.util.List;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.surgingsystems.etl.context.EtlContextProvider;
import com.surgingsystems.etl.dsl.filter.RecordPropertyAccessor;

public class EtlExpressionHelper {

    private ExpressionParser expressionParser;

    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private RecordPropertyAccessor recordPropertyAccessor = new RecordPropertyAccessor();

    private List<Expression> expressions = new ArrayList<Expression>();

    public EtlExpressionHelper() {
        setupParser();
    }

    public void addExpression(String unevaluatedExpression) {
        Expression expression = expressionParser.parseExpression(unevaluatedExpression);
        expressions.add(expression);
    }

    public Expression parse(String unevaluatedExpression) {
        return expressionParser.parseExpression(unevaluatedExpression);
    }

    public Object evaluate(String unevaluatedExpression) {
        return expressionParser.parseExpression(unevaluatedExpression).getValue(evaluationContext);
    }

    public void evaluate() {
        for (Expression expression : expressions) {
            expression.getValue(evaluationContext);
        }
    }

    private void setupParser() {
        SpelParserConfiguration spelParserConfiguration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this
                .getClass().getClassLoader());
        expressionParser = new SpelExpressionParser(spelParserConfiguration);
        evaluationContext.addPropertyAccessor(recordPropertyAccessor);
    }

    public void applyContext(EtlContextProvider etlContextProvider) {
        etlContextProvider.applyArgumentsToContext(evaluationContext);
    }
}

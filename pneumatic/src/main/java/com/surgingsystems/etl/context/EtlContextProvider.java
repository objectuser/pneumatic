package com.surgingsystems.etl.context;

import java.util.Map;

import org.springframework.expression.spel.support.StandardEvaluationContext;

public interface EtlContextProvider {

    void setContextArguments(Map<String, String> properties);

    void applyArgumentsToContext(StandardEvaluationContext evaluationContext);

}

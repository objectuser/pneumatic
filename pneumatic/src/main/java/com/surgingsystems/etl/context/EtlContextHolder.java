package com.surgingsystems.etl.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class EtlContextHolder implements EtlContextProvider, PropertyAccessor {

    private Map<String, String> properties = new HashMap<String, String>();

    @Override
    public void setContextArguments(Map<String, String> properties) {
        this.properties.putAll(properties);
    }

    @Override
    public void applyArgumentsToContext(StandardEvaluationContext evaluationContext) {
        evaluationContext.addPropertyAccessor(this);
        evaluationContext.setVariable("job", this);
    }

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class<?>[] { EtlContextHolder.class };
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return true;
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        EtlContextHolder record = (EtlContextHolder) target;
        String value = record.properties.get(name);
        return new TypedValue(value);
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return false;
    }

    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
    }
}

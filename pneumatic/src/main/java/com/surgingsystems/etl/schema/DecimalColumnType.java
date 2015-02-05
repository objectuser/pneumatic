package com.surgingsystems.etl.schema;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class DecimalColumnType extends ConvertableColumnType<Double> {
    
    private Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    
    @Override
    public Class<Double> getCoreType() {
        return Double.class;
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), "Decimal");
    }

    @Override
    protected boolean isValueCompatible(String value) {
        return pattern.matcher(value).matches();
    }
}

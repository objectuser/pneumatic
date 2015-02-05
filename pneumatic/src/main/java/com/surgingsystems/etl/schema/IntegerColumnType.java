package com.surgingsystems.etl.schema;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class IntegerColumnType extends ConvertableColumnType<Integer> {
    
    private Pattern pattern = Pattern.compile("^-?\\d+$");

    @Override
    public Class<Integer> getCoreType() {
        return Integer.class;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), "Integer");
    }

    @Override
    protected boolean isValueCompatible(String value) {
        return pattern.matcher(value).matches();
    }
}

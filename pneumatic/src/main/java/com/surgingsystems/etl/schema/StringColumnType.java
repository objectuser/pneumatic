package com.surgingsystems.etl.schema;

import org.springframework.stereotype.Component;

@Component
public class StringColumnType extends ConvertableColumnType<String> {
    
    @Override
    public Class<String> getCoreType() {
        return String.class;
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), "String");
    }

    @Override
    protected boolean isValueCompatible(String value) {
        return true;
    }
}

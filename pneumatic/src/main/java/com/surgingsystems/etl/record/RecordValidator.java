package com.surgingsystems.etl.record;


public interface RecordValidator {
    
    boolean accepts(Record record);
}

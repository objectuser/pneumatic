package com.surgingsystems.etl.record;


public interface RecordAggregator {

    String[] aggregate(Record record);
}

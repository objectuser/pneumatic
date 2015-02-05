package com.surgingsystems.etl.record;

import com.surgingsystems.etl.schema.Schema;

public interface RecordWriter {
    
    String write(Record record, Schema schema);
}

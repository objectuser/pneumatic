package com.surgingsystems.etl.dsl.testing;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

public interface RecordFactory extends Iterable<Record> {

    void setSchema(Schema schema);
}

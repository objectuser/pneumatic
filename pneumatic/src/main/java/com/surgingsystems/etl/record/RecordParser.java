package com.surgingsystems.etl.record;

import com.surgingsystems.etl.schema.Schema;

public interface RecordParser<T> {

    Record parse(T input, Schema schema);
}

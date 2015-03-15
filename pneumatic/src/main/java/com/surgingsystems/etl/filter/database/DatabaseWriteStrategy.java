package com.surgingsystems.etl.filter.database;

import javax.sql.DataSource;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

public interface DatabaseWriteStrategy {

    void initialize(DataSource dataSource, Schema schema);

    void write(Record record);

    void close();
}

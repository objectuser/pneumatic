package com.surgingsystems.etl.testing;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import com.surgingsystems.etl.dsl.testing.RecordFactory;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

public class TestDataSet implements Iterable<Record> {

    private Schema schema;

    private RecordFactory recordFactory;

    @Override
    public Iterator<Record> iterator() {
        return recordFactory.iterator();
    }
    
    @PostConstruct
    public void completeConstruction() {
        recordFactory.setSchema(schema);
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Iterator<Record> getRecords() {
        return recordFactory.iterator();
    }

    public RecordFactory getRecordFactory() {
        return recordFactory;
    }

    public void setRecordFactory(RecordFactory recordFactory) {
        this.recordFactory = recordFactory;
    }
}

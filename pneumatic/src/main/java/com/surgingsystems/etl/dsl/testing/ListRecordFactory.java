package com.surgingsystems.etl.dsl.testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class ListRecordFactory implements RecordFactory {

    private Schema schema;

    private List<Record> records = new ArrayList<Record>();

    @Override
    public Iterator<Record> iterator() {
        return new Iterator<Record>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return records.size() > (index - 1);
            }

            @Override
            public Record next() {
                if (hasNext()) {
                    ++index;
                    return records.get(index);
                } else {
                    return null;
                }
            }

        };
    }

    @Override
    public void setSchema(Schema schema) {
        this.schema = schema;

        reconcileColumns();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        for (Record record : records) {
            this.records.add(record);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void reconcileColumns() {
        for (Record record : records) {
            for (Column column : record) {
                ColumnDefinition columnDefinition = schema.getColumnForName(column.getName());
                column.setColumnDefinition(columnDefinition);
            }
        }
    }
}

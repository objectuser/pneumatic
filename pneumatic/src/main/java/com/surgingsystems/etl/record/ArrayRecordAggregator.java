package com.surgingsystems.etl.record;

import java.util.ArrayList;

import com.surgingsystems.etl.schema.Column;

public class ArrayRecordAggregator implements RecordAggregator {

    @Override
    public String[] aggregate(Record record) {
        ArrayList<String> result = new ArrayList<String>();
        for (Column<?> column : record) {
            Object value = column.getValue();
            if (value == null) {
                result.add(null);
            } else {
            result.add(value.toString());
            }
        }
        return result.toArray(new String[0]);
    }

}

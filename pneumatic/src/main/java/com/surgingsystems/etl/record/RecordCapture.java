package com.surgingsystems.etl.record;

import java.util.Arrays;

public class RecordCapture {
    
    public static String capture(Record record) {
        ArrayRecordAggregator arrayRecordAggregator = new ArrayRecordAggregator();
        String capture =  Arrays.toString(arrayRecordAggregator.aggregate(record));
        return capture.substring(0, Math.min(capture.length(), 100));
    }

}

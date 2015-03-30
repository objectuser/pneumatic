package com.surgingsystems.etl.pipe;

import java.util.ArrayList;
import java.util.List;

import com.surgingsystems.etl.record.Record;

public class PipeUtility {

    /**
     * Create a list based on the pipe. This drains the pipe.
     */
    public static List<Record> toList(Pipe pipe) {
        List<Record> result = new ArrayList<Record>();
        while (!pipe.isComplete()) {
            Record record = pipe.pull();
            if (record != null) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * Drain the pipe of all records.
     */
    public static void drain(Pipe pipe) {
        while (!pipe.isComplete()) {
            pipe.pull();
        }
    }
}

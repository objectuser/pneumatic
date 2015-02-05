package com.surgingsystems.etl.test.filter.pipe;

import java.util.ArrayList;
import java.util.List;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class PipeUtility {

    /**
     * Create a list based on the pipe.  This drains the pipe.
     */
    public static List<Record> toList(Pipe pipe) {
        List<Record> result = new ArrayList<Record>();
        while (!pipe.isComplete()) {
            result.add(pipe.pull());
        }
        return result;
    }

}

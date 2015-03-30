package com.surgingsystems.etl.test.filter;

import java.util.ArrayList;
import java.util.List;

import com.surgingsystems.etl.filter.GuardedFilter;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class CannedDataFilter extends GuardedFilter {

    private List<Record> records = new ArrayList<Record>();

    private Pipe output;

    @Override
    protected void process() throws Exception {
        for (Record record : records) {
            output.put(record);
        }
    }

    @Override
    protected void cleanUp() throws Exception {
        output.closedForInput();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void setOutput(Pipe pipe) {
        this.output = pipe;
    }

    public Pipe getOutput() {
        return output;
    }
}

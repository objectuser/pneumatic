package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class PipeRejectRecordStrategy implements RejectRecordStrategy {

    private Pipe output;

    public PipeRejectRecordStrategy() {
    }

    public PipeRejectRecordStrategy(Pipe pipe) {
        this.output = pipe;
    }

    @Override
    public void rejected(Record record) {
        output.put(record);
    }

    @Override
    public void close() {
        output.closedForInput();
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe pipe) {
        this.output = pipe;
    }
}

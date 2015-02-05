package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class PipeRejectRecordStrategy implements RejectRecordStrategy {
    
    private Pipe pipe;
    
    public PipeRejectRecordStrategy() {
    }

    public PipeRejectRecordStrategy(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void rejected(Record record) {
        pipe.put(record);
    }

    @Override
    public void close() {
        pipe.closedForInput();
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }
}

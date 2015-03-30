package com.surgingsystems.etl.filter;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.pipe.PipeUtility;
import com.surgingsystems.etl.record.Record;

/**
 * Handle processing a single input.
 */
public abstract class SingleInputFilter extends GuardedFilter {

    private Pipe input;

    public void setInput(Pipe input) {
        this.input = input;
    }

    public Pipe getInput() {
        return input;
    }

    @Override
    protected void process() throws Exception {
        try {
            preProcess();
            do {
                Record record = input.pull();
                if (record != null) {
                    processRecord(record);
                }
            } while (!input.isComplete());
        } catch (Exception e) {
            PipeUtility.drain(getInput());
            throw e;
        }
    }

    protected abstract void processRecord(Record record) throws Exception;
}

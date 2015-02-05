package com.surgingsystems.etl.filter;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

/**
 * Handle processing a single input.
 */
public abstract class SingleInputFilter extends GuardedFilter implements InputFilter {

    private Pipe input;

    @Override
    public void addInput(Pipe pipe) {
        setInput(pipe);
    }

    public void setInput(Pipe input) {
        this.input = input;
    }

    public Pipe getInput() {
        return input;
    }

    @Override
    protected void filter() throws Exception {
        preProcess();
        do {
            Record record = input.pull();
            if (record != null) {
                process(record);
            }
        } while (!input.isComplete());
        postProcess();
    }

    protected void preProcess() throws Exception {
    }

    protected abstract void process(Record record) throws Exception;

    protected abstract void postProcess() throws Exception;
}

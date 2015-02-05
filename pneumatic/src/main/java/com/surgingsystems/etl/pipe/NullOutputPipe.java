package com.surgingsystems.etl.pipe;

import com.surgingsystems.etl.record.Record;

/**
 * A pipe that acts a place holder but discards all input.
 */
public class NullOutputPipe implements Pipe {
    
    @Override
    public void put(Record record) {
    }

    @Override
    public Record pull() {
	return null;
    }

    @Override
    public void closedForInput() {
    }

    @Override
    public boolean isComplete() {
	return true;
    }

}

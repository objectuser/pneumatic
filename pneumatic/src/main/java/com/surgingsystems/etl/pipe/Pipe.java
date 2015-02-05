package com.surgingsystems.etl.pipe;

import com.surgingsystems.etl.record.Record;

public interface Pipe {

    void put(Record record);

    Record pull();

    void closedForInput();

    boolean isComplete();
}

package com.surgingsystems.etl.filter;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

@SuppressWarnings("unused")
public class LookupFilter extends SingleInputFilter {

    private Pipe output;

    private Pipe outputToLookup;

    private Pipe inputFromLookup;

    @Override
    protected void process(Record record) throws Exception {
        outputToLookup.put(record);
    }

    @Override
    protected void postProcess() throws Exception {
    }

}

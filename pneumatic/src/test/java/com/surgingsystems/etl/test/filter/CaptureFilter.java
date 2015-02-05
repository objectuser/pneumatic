package com.surgingsystems.etl.test.filter;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surgingsystems.etl.filter.SingleInputFilter;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class CaptureFilter extends SingleInputFilter {

    private Logger logger = LogManager.getFormatterLogger(CaptureFilter.class);

    private List<Record> records = new ArrayList<Record>();
    
    private Pipe input;

    @Override
    protected void process(Record record) throws Exception {
        logger.debug("Capturing record " + record);
        records.add(record);
    }

    @Override
    protected void postProcess() throws Exception {
        logger.debug("Capture complete");
    }

    public List<Record> getRecords() {
        return records;
    }

    @Override
    public Pipe getInput() {
        return input;
    }
}

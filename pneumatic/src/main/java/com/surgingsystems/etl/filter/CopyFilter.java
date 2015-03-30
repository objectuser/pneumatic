package com.surgingsystems.etl.filter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

/**
 * Copy a single input to multiple outputs. Copy filters have the following
 * features:
 * <ul>
 * <li>A single input.</li>
 * <li>Any number of outputs greater than zero.</li>
 * <li>The outputs contain copies of the input, unchanged.</li>
 * </ul>
 */
public class CopyFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(CopyFilter.class);

    private List<Pipe> outputs = new ArrayList<Pipe>();

    public CopyFilter() {
    }

    public CopyFilter(String name) {
        setName(name);
    }

    @PostConstruct
    public void validate() {
        logger.trace("Copy (%s): validating", getName());
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
        Assert.isTrue(outputs.size() > 0, "At least one output pipe is required");
    }

    @Override
    protected void processRecord(Record record) throws Exception {

        recordProcessed();
        logRecord(record);

        for (Pipe output : outputs) {
            output.put(record);
        }
    }

    @Override
    protected void postProcess() {
        logSummary();
    }

    @Override
    protected void cleanUp() throws Exception {
        for (Pipe output : outputs) {
            output.closedForInput();
        }
    }

    public List<Pipe> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Pipe> outputs) {
        this.outputs = outputs;
    }
}

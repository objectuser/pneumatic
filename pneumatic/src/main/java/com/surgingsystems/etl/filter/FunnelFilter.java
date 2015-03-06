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
 * Combine multiple inputs into a single output. Funnel filters have the
 * following features:
 * <ul>
 * <li>Any number of inputs.</li>
 * <li>A single output.</li>
 * </ul>
 */
public class FunnelFilter extends GuardedFilter {

    private static Logger logger = LogManager.getFormatterLogger(CopyFilter.class);

    private List<Pipe> inputs = new ArrayList<Pipe>();

    private Pipe output;

    public FunnelFilter() {
    }

    public FunnelFilter(String name) {
        setName(name);
    }

    @PostConstruct
    public void validate() {
        logger.trace("Funnel (%s): validating", getName());
        Assert.notNull(getName(), "The name is required");
        Assert.isTrue(inputs.size() > 0, "At least one input is required");
        Assert.notNull(output, "The output schema is required");
    }

    @Override
    protected void filter() throws Exception {

        while (!allInputsComplete()) {

            for (Pipe input : inputs) {

                if (!input.isComplete()) {
                    Record record = input.pull();
                    if (record != null) {

                        logRecord(record);
                        recordProcessed();

                        output.put(record);
                    }
                }
            }
        }

        output.closedForInput();

        logSummary();
    }

    private boolean allInputsComplete() {
        boolean result = true;
        for (Pipe input : inputs) {
            if (!input.isComplete()) {
                result = false;
                break;
            }
        }

        return result;
    }

    public List<Pipe> getInputs() {
        return inputs;
    }

    public void setInputs(List<Pipe> inputs) {
        this.inputs = inputs;
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }
}

package com.surgingsystems.etl.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

/**
 * Sorts the input according the the supplied comparator and writes the records
 * to the output. Sort filters have the following features:
 * <ul>
 * <li>A single input.</li>
 * <li>A single output.</li>
 * <li>Values on the input are collected in memory until the input is complete.
 * At that point, the records are sorted and subsequently written to the output.
 * </li>
 * </ul>
 */
public class SortFilter extends SingleInputFilter {

    private static Logger logger = LogManager.getFormatterLogger(SortFilter.class);

    private Pipe output;

    private List<Record> records = new ArrayList<Record>();

    private Comparator<Record> comparator;

    public SortFilter() {
    }

    public SortFilter(String name) {
        setName(name);
    }

    @PostConstruct
    public void validate() {
        logger.trace("Sort (%s): validating", getName());
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(getInput(), "The input pipe is required");
        Assert.notNull(output, "The output pipe is required");
        Assert.notNull(comparator, "The comparator is required");
    }

    @Override
    protected void process(Record record) throws Exception {
        recordProcessed();
        logRecord(record);

        records.add(record);
    }

    @Override
    protected void postProcess() {
        Collections.sort(records, comparator);
        for (Record record : records) {
            output.put(record);
        }

        output.closedForInput();

        logSummary();
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public Comparator<Record> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Record> comparator) {
        this.comparator = comparator;
    }
}

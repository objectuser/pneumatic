package com.surgingsystems.etl.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordCapture;

/**
 * A filter base class that handles exceptions.
 */
public abstract class GuardedFilter implements RunnableFilter {

    private static Logger logger = LogManager.getFormatterLogger(GuardedFilter.class);

    private String name;

    private long recordsProcessed = 0L;

    @Override
    public void run() {
        try {

            filter();

            logger.info("%s is shutting down", getName());

        } catch (Exception exception) {
            logger.error(String.format("Error processing filter (%s)", getName()), exception);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Override to implement subclass behavior.
     */
    protected abstract void filter() throws Exception;

    protected void recordProcessed() {
        ++recordsProcessed;
    }

    protected void logSummary() {
        if (logger.isInfoEnabled()) {
            logger.info("%s processed a total of %d records", getName(), recordsProcessed);
        }
    }

    protected void logRecord(Record record) throws Exception {
        if (logger.isTraceEnabled()) {
            logger.trace("%s processing [%s...]", getName(), RecordCapture.capture(record));
        }
    }
}

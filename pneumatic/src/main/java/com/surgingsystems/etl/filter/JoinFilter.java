package com.surgingsystems.etl.filter;

import java.util.Comparator;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

/**
 * Join the inputs using the supplied comparator.
 * <ul>
 * <li>Two inputs: a right input and a left input. The inputs must be sorted
 * according to the comparator prior to their use in this stage.</li>
 * <li>A single output.</li>
 * <li>A comparator that compares values from each input.</li>
 * <li>Inner and left outer join operations.</li>
 * </ul>
 */
public class JoinFilter extends GuardedFilter {

    private static Logger logger = LogManager.getFormatterLogger(JoinFilter.class);

    private Pipe leftInput;

    private Pipe rightInput;

    private Pipe output;

    private Comparator<Record> comparator;

    private Schema outputSchema;

    private JoinStrategy joinStrategy = new InnerJoin();

    public JoinFilter() {
    }

    public JoinFilter(String name) {
        setName(name);
    }

    @PostConstruct
    public void validate() {
        logger.trace("Join (%s): validating", getName());
        Assert.notNull(leftInput, "Left input is required");
        Assert.notNull(rightInput, "Right input is required");
        Assert.notNull(outputSchema, "Output schema is required");
    }

    public void setInnerJoin(boolean ignored) {
        joinStrategy = new InnerJoin();
    }

    public void setLeftOuterJoin(boolean ignored) {
        joinStrategy = new LeftOuterJoin();
    }

    @Override
    protected void process() throws Exception {
        joinStrategy.join();
        logSummary();
    }

    @Override
    protected void cleanUp() throws Exception {
        output.closedForInput();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Record createOutputRecord(Record r1, Record r2) {
        DataRecord result = new DataRecord();
        for (ColumnDefinition columnDefinition : outputSchema) {
            boolean b1 = r1 != null && r1.hasColumnFor(columnDefinition);
            boolean b2 = r2 != null && r2.hasColumnFor(columnDefinition);
            if (b1 && b2) {
                logger.trace("Both inputs have a column named '%s', choosing the first one", columnDefinition.getName());
            }

            if (b1) {
                Column c1 = r1.getColumnFor(columnDefinition);
                result.addColumn(c1);
            } else if (b2) {
                Column c2 = r2.getColumnFor(columnDefinition);
                result.addColumn(c2);
            } else {
                logger.warn("Neither input has a column matching schema column named '%s'", columnDefinition.getName());
            }
        }
        return result;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public Pipe getLeftInput() {
        return leftInput;
    }

    public void setLeftInput(Pipe leftInput) {
        this.leftInput = leftInput;
    }

    public Pipe getRightInput() {
        return rightInput;
    }

    public void setRightInput(Pipe rightInput) {
        this.rightInput = rightInput;
    }

    public Comparator<Record> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Record> comparator) {
        this.comparator = comparator;
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    public Pipe getOutput() {
        return output;
    }

    public boolean isLeftOuterJoin() {
        return joinStrategy instanceof LeftOuterJoin;
    }

    private interface JoinStrategy {

        void join() throws Exception;
    }

    private class InnerJoin implements JoinStrategy {

        @Override
        public void join() throws Exception {
            Record r1 = null;
            Record r2 = null;
            do {
                while (r1 == null && !leftInput.isComplete()) {
                    r1 = leftInput.pull();

                    if (r1 != null) {
                        logRecord(r1);
                        recordProcessed();
                    }
                }

                while (r2 == null && !rightInput.isComplete()) {
                    r2 = rightInput.pull();

                    if (r2 != null) {
                        logRecord(r2);
                        recordProcessed();
                    }
                }

                int comparison = comparator.compare(r1, r2);
                if (comparison == 0) {
                    output.put(createOutputRecord(r1, r2));
                    r1 = null;
                    r2 = null;
                } else if (rightInput.isComplete() || comparison < 0) {
                    r1 = null;
                } else {
                    r2 = null;
                }
            } while (!leftInput.isComplete() || !rightInput.isComplete());
        }
    }

    private class LeftOuterJoin implements JoinStrategy {

        @Override
        public void join() throws Exception {
            Record r1 = null;
            Record r2 = null;
            do {
                while (r1 == null && !leftInput.isComplete()) {
                    r1 = leftInput.pull();

                    if (r1 != null) {
                        logRecord(r1);
                        recordProcessed();
                    }
                }

                while (r2 == null && !rightInput.isComplete()) {
                    r2 = rightInput.pull();

                    if (r2 != null) {
                        logRecord(r2);
                        recordProcessed();
                    }
                }

                int comparison = comparator.compare(r1, r2);
                if (comparison == 0) {
                    output.put(createOutputRecord(r1, r2));
                    r1 = null;
                    r2 = null;
                } else if (rightInput.isComplete() || comparison < 0) {
                    output.put(createOutputRecord(r1, null));
                    r1 = null;
                } else {
                    r2 = null;
                }

            } while (!leftInput.isComplete() || !rightInput.isComplete());
        }
    }
}

package com.surgingsystems.etl.testing;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.surgingsystems.etl.filter.RunnableFilter;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;

public class EtlTest {

    private Logger logger = LogManager.getFormatterLogger(EtlTest.class);

    private String name;

    private RunnableFilter filter;

    private Pipe input;

    private TestDataSet inputDataSet;

    private Pipe output;

    private TestDataSet outputDataSet;

    public void run() {
        logger.info("Starting test %s", name);

        for (Record record : inputDataSet) {
            input.put(record);
        }
        
        input.closedForInput();
        filter.run();

        List<Record> expectedOutputRecords = new ArrayList<Record>();
        for (Record record : inputDataSet) {
            expectedOutputRecords.add(record);
        }

        List<Record> outputRecords = new ArrayList<Record>();
        while (!output.isComplete()) {
            Record record = output.pull();
            if (record != null) {
                outputRecords.add(record);
            }
        }
        
        Assert.assertEquals(expectedOutputRecords, outputRecords);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RunnableFilter getFilter() {
        return filter;
    }

    public void setFilter(RunnableFilter filter) {
        this.filter = filter;
    }

    public Pipe getInput() {
        return input;
    }

    public void setInput(Pipe input) {
        this.input = input;
    }

    public TestDataSet getInputDataSet() {
        return inputDataSet;
    }

    public void setInputDataSet(TestDataSet inputDataSet) {
        this.inputDataSet = inputDataSet;
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public TestDataSet getOutputDataSet() {
        return outputDataSet;
    }

    public void setOutputDataSet(TestDataSet outputDataSet) {
        this.outputDataSet = outputDataSet;
    }
}

package com.surgingsystems.etltest;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.AggregatorFilter;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:configs/totals-job.xml" })
public class TotalsAggregatorTest {

    @Resource(name = "averagePriceFilter")
    private AggregatorFilter averagePriceFilter;

    @Resource(name = "copyOutputToAveragePrice")
    private Pipe aggregatorInput;

    @Resource(name = "averagePriceFunctionOutput")
    private Pipe aggregatorOutput;

    @Resource(name = "stockSchema")
    private Schema schema;

    private Double onehundred = 100.0;

    @Test
    public void doSomething() {
        averagePriceFilter.validate();
        DataRecord inputRecord = new DataRecord(schema);
        Column<Double> lastSale = inputRecord.getColumnForName("LastSale");
        lastSale.setValue(onehundred);
        aggregatorInput.put(inputRecord);
        aggregatorInput.closedForInput();
        averagePriceFilter.run();
        Record outputRecord = aggregatorOutput.pull();
        Assert.assertNotNull(outputRecord);
        Column<Double> average = outputRecord.getColumnForName("Average");
        Assert.assertEquals(onehundred, average.getValue());
    }
}

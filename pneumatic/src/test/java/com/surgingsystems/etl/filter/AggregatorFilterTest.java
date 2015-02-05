package com.surgingsystems.etl.filter;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.function.SumFunction;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "aggregator-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AggregatorFilterTest {

    @Autowired
    private AggregatorFilter aggregatorFilter;

    @Resource(name = "input")
    private Pipe input;

    @Resource(name = "inputSchema")
    private Schema inputSchema;

    @Resource(name = "output")
    private Pipe output;

    @Resource(name = "aggregatorOutput")
    private Pipe aggregatorOutput;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        aggregatorFilter.setInput(null);
        aggregatorFilter.validate();
    }

    public void inputSchemaIsNotRequired() {
        aggregatorFilter.setInputSchema(null);
        aggregatorFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        aggregatorFilter.setOutput(null);
        aggregatorFilter.validate();
    }

    public void outputSchemaIsNotRequired() {
        aggregatorFilter.setOutputSchema(null);
        aggregatorFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void functionIsRequired() {
        aggregatorFilter.setFunction(null);
        aggregatorFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void functionInputMustMatchInputSchema() {
        SumFunction<Double> function = (SumFunction<Double>) aggregatorFilter.<Double> getFunction();
        function.getInputColumnDefinition().setName("Wrong Name");
        aggregatorFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void functionOutputMustMatchInputSchema() {
        SumFunction<Double> function = (SumFunction<Double>) aggregatorFilter.<Double> getFunction();
        function.getInputColumnDefinition().setName("Wrong Name");
        aggregatorFilter.validate();
    }

    @Test
    public void processRecordToOutput() {
        DataRecord trance = new DataRecord(inputSchema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(inputSchema, "Bronson", 5, 5000.0);
        input.put(trance);
        input.put(bronson);
        input.closedForInput();
        aggregatorFilter.run();
        Assert.assertFalse("Output is not complete", aggregatorOutput.isComplete());
        Record outputRecord = aggregatorOutput.pull();
        Assert.assertTrue("Output is complete", aggregatorOutput.isComplete());
        Assert.assertEquals(7000.0, outputRecord.<Double> getValueForName("Price"), 0.0);
    }
}

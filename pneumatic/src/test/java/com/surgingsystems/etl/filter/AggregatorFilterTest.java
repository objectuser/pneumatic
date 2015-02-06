package com.surgingsystems.etl.filter;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.function.Function;
import com.surgingsystems.etl.filter.function.SumFunction;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "aggregator-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AggregatorFilterTest {

    @Resource(name = "aggregator1")
    private AggregatorFilter aggregatorFilter1;

    @Resource(name = "aggregator2")
    private AggregatorFilter aggregatorFilter2;

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
        aggregatorFilter1.setInput(null);
        aggregatorFilter1.validate();
    }

    public void inputSchemaIsNotRequired() {
        aggregatorFilter1.setInputSchema(null);
        aggregatorFilter1.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        aggregatorFilter1.setOutput(null);
        aggregatorFilter1.validate();
    }

    public void outputSchemaIsNotRequired() {
        aggregatorFilter1.setOutputSchema(null);
        aggregatorFilter1.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void functionIsRequired() {
        aggregatorFilter1.setFunction(null);
        aggregatorFilter1.validate();
    }

    public void function1IsSumFunction() {
        Function<Double> function = aggregatorFilter1.<Double> getFunction();
        Assert.assertTrue("Function is the sum function", function instanceof SumFunction);
    }

    public void function2IsSumFunction() {
        Function<Double> function = aggregatorFilter2.<Double> getFunction();
        Assert.assertTrue("Function 2 is the sum function", function instanceof SumFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void functionInputMustMatchInputSchema() {
        SumFunction<Double> function = (SumFunction<Double>) aggregatorFilter1.<Double> getFunction();
        function.getInputColumnDefinition().setName("Wrong Name");
        aggregatorFilter1.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void functionOutputMustMatchInputSchema() {
        SumFunction<Double> function = (SumFunction<Double>) aggregatorFilter1.<Double> getFunction();
        function.getInputColumnDefinition().setName("Wrong Name");
        aggregatorFilter1.validate();
    }

    @Test
    public void processRecordToOutput() {
        DataRecord trance = new DataRecord(inputSchema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(inputSchema, "Bronson", 5, 5000.0);
        input.put(trance);
        input.put(bronson);
        input.closedForInput();
        aggregatorFilter1.run();
        Assert.assertFalse("Output is not complete", aggregatorOutput.isComplete());
        Record outputRecord = aggregatorOutput.pull();
        Assert.assertTrue("Output is complete", aggregatorOutput.isComplete());
        Assert.assertEquals(7000.0, outputRecord.<Double> getValueForName("Price"), 0.0);
    }
}

package com.surgingsystems.etl.filter;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.test.filter.pipe.PipeUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "transformer-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TransformerFilterTest {

    @Resource(name = "countingTransformer")
    private TransformerFilter countingTransformer;

    @Resource(name = "validatingTransformer")
    private TransformerFilter validatingTransformer;

    @Resource(name = "input")
    private Pipe input;

    @Resource(name = "output1")
    private Pipe output1;

    @Resource(name = "output2")
    private Pipe output2;

    @Resource(name = "output3")
    private Pipe output3;

    @Resource(name = "inputSchema")
    private Schema schema;

    @Resource(name = "countSchema")
    private Schema countSchema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        countingTransformer.setInput(null);
        countingTransformer.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void atLeastOneOutputIsRequired() {
        countingTransformer.getOutputConfigurations().clear();
        countingTransformer.validate();
    }

    @Test
    public void transform() {

        {
            DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
            DataRecord bronson = new DataRecord(schema, "Bronson", 5, 5000.0);
            DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
            input.put(trance);
            input.put(bronson);
            input.put(mach6);
        }

        input.closedForInput();

        countingTransformer.run();

        {
            List<Record> outputRecords = PipeUtility.toList(output1);

            DataRecord trance = new DataRecord(schema, "Trance", 20, 2000.0);
            DataRecord bronson = new DataRecord(schema, "Bronson", 10, 5000.0);
            DataRecord mach6 = new DataRecord(schema, "Mach6", 30, 6000.0);

            List<Record> expectedOutput = Arrays.asList(trance, bronson, mach6);

            Assert.assertEquals(expectedOutput, outputRecords);
        }

    }

    @Test
    public void validateOrder() {
        DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
        DataRecord bronson = new DataRecord(schema, "Bronson", 5, 5000.0);
        DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
        input.put(mach6);
        input.put(bronson);
        input.put(trance);

        input.closedForInput();

        validatingTransformer.run();

        List<Record> expectedOutput1Records = Arrays.asList(mach6, trance);
        List<Record> output1Records = PipeUtility.toList(output1);
        List<Record> expectedOutput2Records = Arrays.asList(bronson);
        List<Record> output2Records = PipeUtility.toList(output2);
        List<Record> expectedOutput3Records = Arrays.asList(new DataRecord(countSchema, 1));
        List<Record> output3Records = PipeUtility.toList(output3);

        Assert.assertEquals(expectedOutput1Records, output1Records);
        Assert.assertEquals(expectedOutput2Records, output2Records);
        Assert.assertEquals(expectedOutput3Records, output3Records);
    }
}

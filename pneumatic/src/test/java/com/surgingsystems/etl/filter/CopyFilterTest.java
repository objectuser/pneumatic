package com.surgingsystems.etl.filter;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration({ "classpath:etl-context.xml", "copy-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CopyFilterTest {

    @Autowired
    private CopyFilter copyFilter;

    @Autowired
    private Pipe input;

    @Autowired
    private Pipe output1;

    @Autowired
    private Pipe output2;

    @Autowired
    private Schema schema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        copyFilter.setInput(null);
        copyFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void atLeastOneOutputIsRequired() {
        copyFilter.getOutputs().remove(0);
        copyFilter.getOutputs().remove(0);
        copyFilter.validate();
    }

    @Test
    public void copy() {
        DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(schema, "Bronson", 5, 5000.0);
        DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
        input.put(trance);
        input.put(bronson);
        input.put(mach6);
        List<Record> inputRecords = Arrays.asList(trance, bronson, mach6);

        input.closedForInput();

        copyFilter.run();

        List<Record> output1Records = PipeUtility.toList(output1);
        List<Record> output2Records = PipeUtility.toList(output2);

        Assert.assertEquals(inputRecords, output1Records);
        Assert.assertEquals(inputRecords, output2Records);
    }
}

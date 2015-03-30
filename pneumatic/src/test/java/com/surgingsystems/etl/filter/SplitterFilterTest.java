package com.surgingsystems.etl.filter;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.pipe.PipeUtility;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "splitter-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SplitterFilterTest {

    @Autowired
    private SplitterFilter splitterFilter;

    @Resource(name = "input")
    private Pipe input;

    @Resource(name = "output1")
    private Pipe output1;

    @Resource(name = "output2")
    private Pipe output2;

    @Resource(name = "inputSchema")
    private Schema schema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        splitterFilter.setInput(null);
        splitterFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void atLeastOneOutputIsRequired() {
        splitterFilter.getOutputConditions().clear();
        splitterFilter.validate();
    }

    @Test
    public void split() {
        DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(schema, "Bronson", 5, 5000.0);
        DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
        input.put(trance);
        input.put(bronson);
        input.put(mach6);

        input.closedForInput();

        splitterFilter.run();

        List<Record> output1Records = PipeUtility.toList(output1);
        List<Record> output2Records = PipeUtility.toList(output2);

        Assert.assertEquals(Arrays.asList(bronson, mach6), output1Records);
        Assert.assertEquals(Arrays.asList(trance), output2Records);
    }
}

package com.surgingsystems.etl.filter;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "funnel-filter-test.xml" })
public class FunnelFilterTest {

    @Autowired
    private FunnelFilter funnelFilter;

    @Resource(name = "input1")
    private Pipe input1;

    @Resource(name = "input2")
    private Pipe input2;

    @Resource(name = "output")
    private Pipe output;

    @Resource(name = "inputSchema")
    private Schema inputSchema;

    @Test
    public void processRecordToOutput() {
        DataRecord trance = new DataRecord(inputSchema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(inputSchema, "Bronson", 5, 5000.0);
        input1.put(trance);
        input2.put(bronson);
        input1.closedForInput();
        input2.closedForInput();
        funnelFilter.run();
        Assert.assertFalse("Output is not complete", output.isComplete());
        output.pull();
        output.pull();
        Assert.assertTrue("Output is complete", output.isComplete());
    }
}

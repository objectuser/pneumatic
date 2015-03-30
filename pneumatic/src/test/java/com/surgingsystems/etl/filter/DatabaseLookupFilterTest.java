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
@ContextConfiguration({ "classpath:etl-context.xml", "database-lookup-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseLookupFilterTest {

    @Autowired
    private DatabaseLookupFilter databaseLookupFilter;

    @Resource(name = "databaseLookupInput")
    private Pipe input;

    @Resource(name = "mtbSchema")
    private Schema inputSchema;

    @Resource(name = "databaseLookupOutput")
    private Pipe output;

    @Resource(name = "sqlSelectSchema")
    private Schema outputSchema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        databaseLookupFilter.setInput(null);
        databaseLookupFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        databaseLookupFilter.setOutput(null);
        databaseLookupFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputSchemaIsRequired() {
        databaseLookupFilter.setOutputSchema(null);
        databaseLookupFilter.validate();
    }

    @Test
    public void lookup() {

        {
            DataRecord trance = new DataRecord(inputSchema, "Giant Trance", 2014);
            DataRecord bronson = new DataRecord(inputSchema, "Santa Cruz Bronson", 2013);
            DataRecord mach6 = new DataRecord(inputSchema, "Ibis Mojo", 2015);
            input.put(trance);
            input.put(bronson);
            input.put(mach6);
        }

        input.closedForInput();
        databaseLookupFilter.run();

        List<Record> outputRecords = PipeUtility.toList(output);

        {
            DataRecord trance = new DataRecord(outputSchema, "Giant Trance", 2014, 3500.0);
            DataRecord bronson = new DataRecord(outputSchema, "Santa Cruz Bronson", 2013, 4500.0);
            DataRecord mach6 = new DataRecord(outputSchema, "Ibis Mojo", 2015, 5500.0);
            List<Record> inputRecords = Arrays.asList(trance, bronson, mach6);

            Assert.assertEquals(inputRecords, outputRecords);
        }
    }
}

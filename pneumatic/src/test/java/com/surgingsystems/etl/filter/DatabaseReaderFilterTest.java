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
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.test.filter.pipe.PipeUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "database-reader-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseReaderFilterTest {

    @Autowired
    private DatabaseReaderFilter databaseReaderFilter;

    @Resource(name = "databaseReaderOutput")
    private Pipe output;

    @Resource(name = "sqlSelectSchema")
    private Schema outputSchema;

    @Test(expected = IllegalArgumentException.class)
    public void outputPipeIsRequired() {
        databaseReaderFilter.setOutput(null);
        databaseReaderFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputSchemaIsRequired() {
        databaseReaderFilter.setOutputSchema(null);
        databaseReaderFilter.validate();
    }

    @Test
    public void read() {

        databaseReaderFilter.run();

        List<Record> outputRecords = PipeUtility.toList(output);

        DataRecord trance = new DataRecord(outputSchema, "Giant Trance", 2014, 3500.0);
        List<Record> expectedRecords = Arrays.asList(trance);

        Assert.assertEquals(expectedRecords, outputRecords);
    }
}

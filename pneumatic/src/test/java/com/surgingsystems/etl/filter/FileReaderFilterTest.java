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
import com.surgingsystems.etl.pipe.PipeUtility;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "file-reader-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FileReaderFilterTest {

    @Resource(name = "fileReader")
    private FileReaderFilter fileReaderFilter;

    @Resource(name = "fileReaderForBadData")
    private FileReaderFilter fileReaderFilterForBadData;

    @Resource(name = "fileReaderOutput")
    private Pipe output;

    @Resource(name = "fileReaderRejectionOutput")
    private Pipe rejectionOutput;

    @Resource(name = "mtbSchema")
    private Schema outputSchema;

    @Test(expected = IllegalArgumentException.class)
    public void outputPipeIsRequired() {
        fileReaderFilter.setOutput(null);
        fileReaderFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputSchemaIsRequired() {
        fileReaderFilter.setOutputSchema(null);
        fileReaderFilter.validate();
    }

    @Test
    public void read() {

        fileReaderFilter.run();

        List<Record> outputRecords = PipeUtility.toList(output);

        Assert.assertEquals("Read all records", 6, outputRecords.size());

        DataRecord sb6c = new DataRecord(outputSchema, "Yeti SB6c", 2015, 6499.0);
        DataRecord slash = new DataRecord(outputSchema, "Trek Slash", 2014, 6000.0);
        DataRecord stereo = new DataRecord(outputSchema, "Cube Stereo", 2015, 4199.0);
        DataRecord trance = new DataRecord(outputSchema, "Giant Trance", 2014, 3500.0);
        DataRecord bronson = new DataRecord(outputSchema, "Santa Cruz Bronson", 2013, 4500.0);
        DataRecord mojo = new DataRecord(outputSchema, "Ibis Mojo", 2015, 5500.0);
        List<Record> expectedRecords = Arrays.asList(sb6c, slash, stereo, trance, bronson, mojo);

        Assert.assertEquals(expectedRecords, outputRecords);
    }

    @Test
    public void readBadData() {

        fileReaderFilterForBadData.run();

        List<Record> outputRecords = PipeUtility.toList(output);
        List<Record> rejectionRecords = PipeUtility.toList(rejectionOutput);

        Assert.assertEquals("Read all records", 6, outputRecords.size());
        Assert.assertEquals("Rejected one record", 1, rejectionRecords.size());

        DataRecord sb6c = new DataRecord(outputSchema, "Yeti SB6c", 2015, 6499.0);
        DataRecord slash = new DataRecord(outputSchema, "Trek Slash", 2014, 6000.0);
        DataRecord stereo = new DataRecord(outputSchema, "Cube Stereo", 2015, 4199.0);
        DataRecord trance = new DataRecord(outputSchema, "Giant Trance", 2014, 3500.0);
        DataRecord bronson = new DataRecord(outputSchema, "Santa Cruz Bronson", 2013, 4500.0);
        DataRecord mojo = new DataRecord(outputSchema, "Ibis Mojo", 2015, 5500.0);
        List<Record> expectedRecords = Arrays.asList(sb6c, slash, stereo, trance, bronson, mojo);

        Assert.assertEquals(expectedRecords, outputRecords);
    }
}

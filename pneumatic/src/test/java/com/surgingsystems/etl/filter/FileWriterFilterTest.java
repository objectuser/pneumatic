package com.surgingsystems.etl.filter;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "file-writer-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FileWriterFilterTest {

    @Autowired
    private FileWriterFilter fileWriterFilter;

    @Resource(name = "fileReaderOutput")
    private Pipe input;

    @Resource(name = "mtbSchema")
    private Schema mtbSchema;

    @Test(expected = IllegalArgumentException.class)
    public void inputPipeIsRequired() {
        fileWriterFilter.setInput(null);
        fileWriterFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void fileIsRequired() {
        fileWriterFilter.setFileResource(null);
        fileWriterFilter.validate();
    }

    @Test
    public void insert() throws Exception {

        DataRecord sb6c = new DataRecord(mtbSchema, "Yeti SB6c", 2015, 6499.0);
        DataRecord slash = new DataRecord(mtbSchema, "Trek Slash", 2014, 6000.0);
        DataRecord stereo = new DataRecord(mtbSchema, "Cube Stereo", 2015, 4199.0);
        DataRecord trance = new DataRecord(mtbSchema, "Giant Trance", 2014, 3500.0);
        DataRecord bronson = new DataRecord(mtbSchema, "Santa Cruz Bronson", 2013, 4500.0);
        DataRecord mojo = new DataRecord(mtbSchema, "Ibis Mojo", 2015, 5500.0);
        input.put(sb6c);
        input.put(slash);
        input.put(stereo);
        input.put(trance);
        input.put(bronson);
        input.put(mojo);
        input.closedForInput();

        fileWriterFilter.run();

        Assert.assertTrue("File is a copy of input", FileUtils.contentEquals(
                new ClassPathResource("data/mtb.txt").getFile(), new PathResource("output/output1.txt").getFile()));
    }
}

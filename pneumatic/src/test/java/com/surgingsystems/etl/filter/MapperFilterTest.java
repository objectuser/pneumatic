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
@ContextConfiguration({ "classpath:etl-context.xml", "mapper-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MapperFilterTest {

    @Resource(name = "simpleMapper")
    private MapperFilter simpleMapperFilter;

    @Resource(name = "explicitMapper")
    private MapperFilter explicitMapperFilter;

    @Resource(name = "input")
    private Pipe input;

    @Resource(name = "output")
    private Pipe output;

    @Resource(name = "giantBikesSchema")
    private Schema giantBikesSchema;

    @Resource(name = "simpleSchema")
    private Schema simpleSchema;

    @Resource(name = "explicitSchema")
    private Schema explicitSchema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        simpleMapperFilter.setInput(null);
        simpleMapperFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        simpleMapperFilter.setOutput(null);
        simpleMapperFilter.validate();
    }

    @Test
    public void simpleMap() {

        {
            DataRecord sb6c = new DataRecord(giantBikesSchema, "Yeti SB6c", 100001, 2015, 6499.0);
            DataRecord slash = new DataRecord(giantBikesSchema, "Trek Slash", 100002, 2014, 6000.0);
            DataRecord stereo = new DataRecord(giantBikesSchema, "Cube Stereo", 100003, 2015, 4199.0);
            DataRecord trance = new DataRecord(giantBikesSchema, "Giant Trance", 100004, 2014, 3500.0);
            DataRecord bronson = new DataRecord(giantBikesSchema, "Santa Cruz Bronson", 100005, 2013, 4500.0);
            DataRecord mojo = new DataRecord(giantBikesSchema, "Ibis Mojo", 100006, 2015, 5500.0);
            input.put(sb6c);
            input.put(slash);
            input.put(stereo);
            input.put(trance);
            input.put(bronson);
            input.put(mojo);
        }

        input.closedForInput();

        simpleMapperFilter.run();

        {
            List<Record> outputRecords = PipeUtility.toList(output);

            DataRecord sb6c = new DataRecord(simpleSchema, "Yeti SB6c", "100001", 2015, 6499.0);
            DataRecord slash = new DataRecord(simpleSchema, "Trek Slash", "100002", 2014, 6000.0);
            DataRecord stereo = new DataRecord(simpleSchema, "Cube Stereo", "100003", 2015, 4199.0);
            DataRecord trance = new DataRecord(simpleSchema, "Giant Trance", "100004", 2014, 3500.0);
            DataRecord bronson = new DataRecord(simpleSchema, "Santa Cruz Bronson", "100005", 2013, 4500.0);
            DataRecord mojo = new DataRecord(simpleSchema, "Ibis Mojo", "100006", 2015, 5500.0);

            List<Record> expectedOutput = Arrays.asList(sb6c, slash, stereo, trance, bronson, mojo);

            Assert.assertEquals(expectedOutput, outputRecords);
        }
    }

    @Test
    public void explicitMap() {

        {
            DataRecord sb6c = new DataRecord(giantBikesSchema, "Yeti SB6c", 100001, 2015, 6499.0);
            DataRecord slash = new DataRecord(giantBikesSchema, "Trek Slash", 100002, 2014, 6000.0);
            DataRecord stereo = new DataRecord(giantBikesSchema, "Cube Stereo", 100003, 2015, 4199.0);
            DataRecord trance = new DataRecord(giantBikesSchema, "Giant Trance", 100004, 2014, 3500.0);
            DataRecord bronson = new DataRecord(giantBikesSchema, "Santa Cruz Bronson", 100005, 2013, 4500.0);
            DataRecord mojo = new DataRecord(giantBikesSchema, "Ibis Mojo", 100006, 2015, 5500.0);
            input.put(sb6c);
            input.put(slash);
            input.put(stereo);
            input.put(trance);
            input.put(bronson);
            input.put(mojo);
        }

        input.closedForInput();

        explicitMapperFilter.run();

        {
            List<Record> outputRecords = PipeUtility.toList(output);

            DataRecord sb6c = new DataRecord(explicitSchema, "Yeti SB6c", "100001", 2015, 6499.0);
            DataRecord slash = new DataRecord(explicitSchema, "Trek Slash", "100002", 2014, 6000.0);
            DataRecord stereo = new DataRecord(explicitSchema, "Cube Stereo", "100003", 2015, 4199.0);
            DataRecord trance = new DataRecord(explicitSchema, "Giant Trance", "100004", 2014, 3500.0);
            DataRecord bronson = new DataRecord(explicitSchema, "Santa Cruz Bronson", "100005", 2013, 4500.0);
            DataRecord mojo = new DataRecord(explicitSchema, "Ibis Mojo", "100006", 2015, 5500.0);

            List<Record> expectedOutput = Arrays.asList(sb6c, slash, stereo, trance, bronson, mojo);

            Assert.assertEquals(expectedOutput, outputRecords);
        }
    }
}

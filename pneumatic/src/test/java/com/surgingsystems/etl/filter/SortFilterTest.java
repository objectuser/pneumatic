package com.surgingsystems.etl.filter;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "sort-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SortFilterTest {

    @Autowired
    private SortFilter sortFilter;

    @Autowired
    private Pipe input;

    @Autowired
    private Pipe output;

    @Autowired
    private Schema schema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        sortFilter.setInput(null);
        sortFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        sortFilter.setOutput(null);
        sortFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void comparatorIsRequired() {
        sortFilter.setComparator(null);
        sortFilter.validate();
    }

    @Test
    public void sort() {
        DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(schema, "Bronson", 5, 5000.0);
        DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
        input.put(trance);
        input.put(bronson);
        input.put(mach6);
        input.closedForInput();
        sortFilter.run();
        Record r1 = output.pull();
        Record r2 = output.pull();
        Record r3 = output.pull();
        Assert.assertEquals(bronson.getColumnForName("Name"), r1.getColumnForName("Name"));
        Assert.assertEquals(mach6.getColumnForName("Name"), r2.getColumnForName("Name"));
        Assert.assertEquals(trance.getColumnForName("Name"), r3.getColumnForName("Name"));
    }
}

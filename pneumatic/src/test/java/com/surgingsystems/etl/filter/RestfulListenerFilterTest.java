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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.pipe.PipeUtility;
import com.surgingsystems.etl.record.JsonRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "restful-listener-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RestfulListenerFilterTest {

    @Resource(name = "restfulListener")
    private RestfulListenerFilter restfulListenerFilter;

    @Resource(name = "restfulListenerOutput")
    private Pipe output;

    @Resource(name = "outputSchema")
    private Schema schema;

    private ObjectMapper mapper = new ObjectMapper();

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        restfulListenerFilter.setPath(null);
        restfulListenerFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        restfulListenerFilter.setOutput(null);
        restfulListenerFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputSchemaIsRequired() {
        restfulListenerFilter.setOutputSchema(null);
        restfulListenerFilter.validate();
    }

    @Test
    public void accept() throws Exception {
        JsonRecord bronson = new JsonRecord(schema, "Bronson", 5, 5000.0);
        JsonRecord mach6 = new JsonRecord(schema, "Mach6", 15, 6000.0);
        JsonRecord trance = new JsonRecord(schema, "Trance", 10, 2000.0);

        restfulListenerFilter.filter("one", mapper.writeValueAsString(bronson.toMap()));
        restfulListenerFilter.filter("one", mapper.writeValueAsString(mach6.toMap()));
        restfulListenerFilter.filter("one", mapper.writeValueAsString(trance.toMap()));

        output.closedForInput();

        List<Record> outputs = PipeUtility.toList(output);
        List<Record> expected = Arrays.asList(bronson, mach6, trance);
        JsonRecord outputBronson = (JsonRecord) outputs.get(0);
        for (int i = 0; i < bronson.getColumns().size(); ++i) {
            Column<?> e = bronson.getColumns().get(i);
            Column<?> a = outputBronson.getColumns().get(i);
            Assert.assertEquals(e.getName(), a.getName());
            Assert.assertEquals(e.getValue(), a.getValue());
            Assert.assertEquals(e.hashCode(), a.hashCode());
        }

        Assert.assertEquals(bronson.hashCode(), outputBronson.hashCode());
        Assert.assertEquals(bronson, outputBronson);
        Assert.assertEquals(expected, outputs);
    }
}

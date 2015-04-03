package com.surgingsystems.etl.yamldsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.DatabaseReaderFilter;
import com.surgingsystems.etl.filter.mapping.PipeRejectRecordStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:datasource.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseReaderFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parseFile() {
        yamlParser.parse("database-reader-test.yml");

        DatabaseReaderFilter databaseReader = applicationContext.getBean("databaseReader", DatabaseReaderFilter.class);
        Assert.assertNotNull("Found the bean", databaseReader);
        databaseReader.validate();

        Assert.assertNotNull("Name is set", databaseReader.getName());
        Assert.assertEquals("Name is right", "Database Reader", databaseReader.getName());
        Assert.assertNotNull("Output is set", databaseReader.getOutput());
        Assert.assertNotNull("Output schema set", databaseReader.getOutputSchema());
        Assert.assertNotNull("SQL is set", databaseReader.getSql());
        Assert.assertNotNull("Parameters are set", databaseReader.getParameters());
        Assert.assertTrue("Parameters are not empty", databaseReader.getParameters().size() > 0);
        Assert.assertTrue("Reject by pipe",
                databaseReader.getRejectRecordStrategy() instanceof PipeRejectRecordStrategy);
    }

}

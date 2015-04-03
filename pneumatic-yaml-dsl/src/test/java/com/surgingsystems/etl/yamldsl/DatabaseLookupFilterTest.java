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

import com.surgingsystems.etl.filter.DatabaseLookupFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:datasource.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseLookupFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parseFile() {
        yamlParser.parse("database-lookup-test.yml");

        DatabaseLookupFilter databaseLookup = applicationContext.getBean("databaseLookup", DatabaseLookupFilter.class);
        Assert.assertNotNull("Found the bean", databaseLookup);
        databaseLookup.validate();

        Assert.assertNotNull("Name is set", databaseLookup.getName());
        Assert.assertEquals("Name is right", "Database Lookup", databaseLookup.getName());
        Assert.assertNotNull("Input is set", databaseLookup.getInput());
        Assert.assertNotNull("Input schema is set", databaseLookup.getInput());
        Assert.assertNotNull("Output is set", databaseLookup.getOutput());
        Assert.assertNotNull("Output schema set", databaseLookup.getOutputSchema());
        Assert.assertNotNull("SQL is set", databaseLookup.getSql());
        Assert.assertNotNull("Parameters are set", databaseLookup.getParameters());
        Assert.assertTrue("Parameters are not empty", databaseLookup.getParameters().size() > 0);
    }
}

package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.DatabaseReaderFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "database-reader-test.xml" })
public class DatabaseReaderBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        DatabaseReaderFilter databaseReader = applicationContext.getBean("databaseReader", DatabaseReaderFilter.class);
        Assert.assertNotNull("Found the bean", databaseReader);
        databaseReader.validate();

        Assert.assertNotNull("Name is set", databaseReader.getName());
        Assert.assertEquals("Name is right", "Database Reader", databaseReader.getName());
        Assert.assertNotNull("Output is set", databaseReader.getOutput());
        Assert.assertNotNull("Output schema set", databaseReader.getOutputSchema());
        Assert.assertNotNull("SQL is set", databaseReader.getSqlSelect());
        Assert.assertNotNull("Parameters are set", databaseReader.getParameters());
        Assert.assertTrue("Parameters are not empty", databaseReader.getParameters().size() > 0);
    }

}

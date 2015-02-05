package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.DatabaseWriterFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "database-writer-update-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseWriterUpdateBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        DatabaseWriterFilter databaseWriter = applicationContext.getBean("databaseWriter", DatabaseWriterFilter.class);
        Assert.assertNotNull("Found the bean", databaseWriter);
        databaseWriter.validate();

        Assert.assertNotNull("Name is set", databaseWriter.getName());
        Assert.assertEquals("Name is right", "Database Writer", databaseWriter.getName());
        Assert.assertNotNull("Output is set", databaseWriter.getInput());
        Assert.assertNotNull("Output schema set", databaseWriter.getInput());
        Assert.assertNotNull("Data source is set", databaseWriter.getDataSource());
    }

}

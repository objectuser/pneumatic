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

import com.surgingsystems.etl.filter.DatabaseWriterFilter;
import com.surgingsystems.etl.filter.database.ConfigurableDatabaseWriteStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:datasource.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseWriterFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void insert() {
        yamlParser.parse("database-writer-insert-test.yml");

        DatabaseWriterFilter databaseWriter = applicationContext.getBean("databaseWriter", DatabaseWriterFilter.class);
        Assert.assertNotNull("Found the bean", databaseWriter);
        databaseWriter.validate();

        Assert.assertNotNull("Name is set", databaseWriter.getName());
        Assert.assertEquals("Name is right", "Database Writer", databaseWriter.getName());
        Assert.assertNotNull("Input is set", databaseWriter.getInput());
        Assert.assertNotNull("Input schema set", databaseWriter.getInput());
        Assert.assertNotNull("SQL is set", databaseWriter.getDataSource());
    }

    @Test
    public void update() {
        yamlParser.parse("database-writer-update-test.yml");

        DatabaseWriterFilter databaseWriter = applicationContext.getBean("databaseWriter", DatabaseWriterFilter.class);
        Assert.assertNotNull("Found the bean", databaseWriter);
        databaseWriter.validate();

        Assert.assertNotNull("Name is set", databaseWriter.getName());
        Assert.assertEquals("Name is right", "Database Writer", databaseWriter.getName());
        Assert.assertNotNull("Input is set", databaseWriter.getInput());
        Assert.assertNotNull("Input schema set", databaseWriter.getInput());
        Assert.assertNotNull("SQL is set", databaseWriter.getDataSource());
        ConfigurableDatabaseWriteStrategy writeStrategy = (ConfigurableDatabaseWriteStrategy) databaseWriter
                .getWriteStrategy();
        Assert.assertEquals(3, writeStrategy.getParameters().size());
    }

}

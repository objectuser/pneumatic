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

import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("schema-test.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SchemaBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        Schema schema = applicationContext.getBean("inputSchema", Schema.class);
        Assert.assertNotNull("Found the bean", schema);

        Assert.assertEquals("Name is right", "Input Schema", schema.getName());
        Assert.assertTrue("Columns are in the schema", schema.getColumnDefinitions().size() > 0);
    }

}

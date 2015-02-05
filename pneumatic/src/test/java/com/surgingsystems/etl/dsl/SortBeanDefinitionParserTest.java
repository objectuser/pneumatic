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

import com.surgingsystems.etl.filter.SortFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "sort-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SortBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        SortFilter sort = applicationContext.getBean("sort", SortFilter.class);
        Assert.assertNotNull("Found the bean", sort);
        sort.validate();

        Assert.assertNotNull("Name is set", sort.getName());
        Assert.assertEquals("Name is right", "Test Sort", sort.getName());
        Assert.assertNotNull("Input is set", sort.getInput());
        Assert.assertNotNull("Output is set", sort.getOutput());
        Assert.assertFalse("Output not a null pipe", sort.getOutput() instanceof NullOutputPipe);
    }

}

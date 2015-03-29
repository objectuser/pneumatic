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

import com.surgingsystems.etl.filter.SortFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SortFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parse() {
        yamlParser.parse("sort-test.yml");

        SortFilter sort = applicationContext.getBean("sort", SortFilter.class);
        Assert.assertNotNull("Found the bean", sort);
        sort.validate();

        Assert.assertNotNull("Name is set", sort.getName());
        Assert.assertEquals("Name is right", "Sort", sort.getName());
        Assert.assertNotNull("Input is set", sort.getInput());
        Assert.assertNotNull("Output is set", sort.getOutput());
        Assert.assertFalse("Output not a null pipe", sort.getOutput() instanceof NullOutputPipe);
    }

}

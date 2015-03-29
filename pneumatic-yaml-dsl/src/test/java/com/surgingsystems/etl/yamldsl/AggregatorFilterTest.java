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

import com.surgingsystems.etl.filter.AggregatorFilter;
import com.surgingsystems.etl.filter.function.SumFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AggregatorFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parseFile() {
        yamlParser.parse("aggregator-test.yml");

        AggregatorFilter aggregator = applicationContext.getBean("aggregator", AggregatorFilter.class);
        Assert.assertNotNull("Found the bean", aggregator);
        aggregator.validate();

        Assert.assertNotNull("Name is set", aggregator.getName());
        Assert.assertEquals("Name is right", "Test Aggregator", aggregator.getName());
        Assert.assertNotNull("Input is set", aggregator.getInput());
        Assert.assertNotNull("Output is set", aggregator.getOutput());
        Assert.assertNotNull("Function is set", aggregator.getFunction());
        Assert.assertTrue("Function is the sum function", aggregator.getFunction() instanceof SumFunction);
    }

}

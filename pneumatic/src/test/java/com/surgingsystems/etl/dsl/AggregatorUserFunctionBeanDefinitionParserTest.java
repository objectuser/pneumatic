package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.AggregatorFilter;
import com.surgingsystems.etl.filter.function.AverageFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "aggregator-user-function-test.xml" })
public class AggregatorUserFunctionBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        AggregatorFilter aggregator = applicationContext.getBean("aggregator", AggregatorFilter.class);
        Assert.assertNotNull("Found the bean", aggregator);
        aggregator.validate();

        Assert.assertNotNull("Name is set", aggregator.getName());
        Assert.assertEquals("Name is right", "Test Aggregator", aggregator.getName());
        Assert.assertNotNull("Input is set", aggregator.getInput());
        Assert.assertNotNull("Output is set", aggregator.getOutput());
        Assert.assertNotNull("Function is set", aggregator.getFunction());
        Assert.assertTrue("Function is the sum function", aggregator.<Double> getFunction() instanceof AverageFunction);
    }

}

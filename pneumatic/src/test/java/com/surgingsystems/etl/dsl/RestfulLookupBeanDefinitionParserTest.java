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

import com.surgingsystems.etl.filter.RestfulLookupFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:etl-context.xml", "restful-lookup-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RestfulLookupBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        RestfulLookupFilter restfulLookup = applicationContext.getBean("restfulLookup", RestfulLookupFilter.class);
        Assert.assertNotNull("Found the bean", restfulLookup);
        restfulLookup.validate();

        Assert.assertNotNull("Name is set", restfulLookup.getName());
        Assert.assertEquals("Name is right", "Test Restful Lookup", restfulLookup.getName());
        Assert.assertNotNull("Request url is set", restfulLookup.getRequestUrl());
        Assert.assertNotNull("Input is set", restfulLookup.getInput());
        Assert.assertNotNull("Input schema is set", restfulLookup.getInputSchema());
        Assert.assertNotNull("Output is set", restfulLookup.getOutput());
        Assert.assertNotNull("Output schema is set", restfulLookup.getOutputSchema());
        Assert.assertNotNull("Response schema is set", restfulLookup.getResponseSchema());
    }

}

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

import com.surgingsystems.etl.filter.RestfulWriterFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "restful-writer-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RestfulWriterBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        RestfulWriterFilter restfulWriterFilter = applicationContext
                .getBean("restfulWriter", RestfulWriterFilter.class);
        Assert.assertNotNull("Found the bean", restfulWriterFilter);
        restfulWriterFilter.validate();

        Assert.assertNotNull("Name is set", restfulWriterFilter.getName());
        Assert.assertEquals("Name is right", "Test Restful Writer", restfulWriterFilter.getName());
        Assert.assertNotNull("Request url is set", restfulWriterFilter.getUrl());
        Assert.assertNotNull("Input is set", restfulWriterFilter.getInput());
        Assert.assertNotNull("Input schema is set", restfulWriterFilter.getInputSchema());
    }

}

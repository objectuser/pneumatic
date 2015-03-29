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

import com.surgingsystems.etl.filter.SplitterFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "splitter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SplitterBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        SplitterFilter splitter = applicationContext.getBean("splitter", SplitterFilter.class);
        Assert.assertNotNull("Found the bean", splitter);
        splitter.validate();

        Assert.assertNotNull("Name is set", splitter.getName());
        Assert.assertEquals("Name is right", "Splitter", splitter.getName());
        Assert.assertNotNull("Input is set", splitter.getInput());
        Assert.assertTrue("Outputs exists", splitter.getOutputConditions().size() > 0);
    }

}

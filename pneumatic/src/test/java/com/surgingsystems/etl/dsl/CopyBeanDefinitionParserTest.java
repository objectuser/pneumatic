package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.CopyFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "copy-test.xml" })
public class CopyBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        CopyFilter copy = applicationContext.getBean("copy", CopyFilter.class);
        Assert.assertNotNull("Found the bean", copy);
        copy.validate();

        Assert.assertNotNull("Name is set", copy.getName());
        Assert.assertEquals("Name is right", "Test Copy", copy.getName());
        Assert.assertNotNull("Input is set", copy.getInput());
        Assert.assertNotNull("Outputs are set", copy.getOutputs());
        Assert.assertTrue("Outputs is not empty", copy.getOutputs().size() > 0);
    }

}

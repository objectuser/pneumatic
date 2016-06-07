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

import com.surgingsystems.etl.filter.CopyFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CopyFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parseFile() {
        yamlParser.parse("copy-test.yml");

        CopyFilter copy = applicationContext.getBean("tripleCopy", CopyFilter.class);
        Assert.assertNotNull("Found the bean", copy);
        copy.validate();

        Assert.assertNotNull("Name is set", copy.getName());
        Assert.assertEquals("Name is right", "Copy in Triplicate", copy.getName());
        Assert.assertNotNull("Input is set", copy.getInput());
        Assert.assertNotNull("Outputs are set", copy.getOutputs());
        Assert.assertTrue("Outputs is not empty", copy.getOutputs().size() > 0);
    }

}

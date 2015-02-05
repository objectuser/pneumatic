package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.MapperFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "modify-test.xml" })
public class ModifierBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        MapperFilter modifier = applicationContext.getBean("mapper", MapperFilter.class);
        Assert.assertNotNull("Found the bean", modifier);
        modifier.validate();

        Assert.assertNotNull("Name is set", modifier.getName());
        Assert.assertEquals("Name is right", "Test Mapper", modifier.getName());
        Assert.assertNotNull("Input is set", modifier.getInput());
        Assert.assertNotNull("Input schema is set", modifier.getInputSchema());
        Assert.assertNotNull("Output is set", modifier.getOutput());
        Assert.assertFalse("Output not a null pipe", modifier.getOutput() instanceof NullOutputPipe);
        Assert.assertNotNull("Mapping is set", modifier.getMapping());
        Assert.assertNotNull("Output schema is set", modifier.getOutputSchema());
    }

}

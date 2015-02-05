package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.MapperFilter;
import com.surgingsystems.etl.filter.mapping.ColumnDefinitionMappingStrategy;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "modify-mappings-test.xml" })
public class ModifierWithMappingBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        MapperFilter mapper = applicationContext.getBean("mapper", MapperFilter.class);
        Assert.assertNotNull("Found the bean", mapper);
        mapper.validate();

        Assert.assertNotNull("Name is set", mapper.getName());
        Assert.assertEquals("Name is right", "Test Mapper", mapper.getName());
        Assert.assertNotNull("Input is set", mapper.getInput());
        Assert.assertNotNull("Input schema is set", mapper.getInputSchema());
        Assert.assertNotNull("Output is set", mapper.getOutput());
        Assert.assertFalse("Output not a null pipe", mapper.getOutput() instanceof NullOutputPipe);
        Assert.assertNotNull("Mapping is set", mapper.getMapping());
        Assert.assertNotNull("Output schema is set", mapper.getOutputSchema());
        Assert.assertTrue("Mapping is by column definition",
                mapper.getMapping().getMappingStrategy() instanceof ColumnDefinitionMappingStrategy);
    }
}

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

import com.surgingsystems.etl.filter.MapperFilter;
import com.surgingsystems.etl.filter.mapping.ColumnDefinitionMappingStrategy;
import com.surgingsystems.etl.filter.mapping.ColumnNameMappingStrategy;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ModifierFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parse() {
        yamlParser.parse("mapper-test.yml");

        {
            MapperFilter mapper = applicationContext.getBean("columnMapper", MapperFilter.class);
            Assert.assertNotNull("Found the bean", mapper);
            mapper.validate();

            Assert.assertNotNull("Name is set", mapper.getName());
            Assert.assertEquals("Name is right", "Column Mapper", mapper.getName());
            Assert.assertNotNull("Input is set", mapper.getInput());
            Assert.assertNotNull("Input schema is set", mapper.getInputSchema());
            Assert.assertNotNull("Output is set", mapper.getOutput());
            Assert.assertFalse("Output not a null pipe", mapper.getOutput() instanceof NullOutputPipe);
            Assert.assertNotNull("Mapping is set", mapper.getMapping());
            Assert.assertNotNull("Output schema is set", mapper.getOutputSchema());
            Assert.assertTrue("Mapping is by column definition",
                    mapper.getMapping().getMappingStrategy() instanceof ColumnDefinitionMappingStrategy);
        }

        {
            MapperFilter mapper = applicationContext.getBean("nameMapper", MapperFilter.class);
            Assert.assertNotNull("Found the bean", mapper);
            mapper.validate();

            Assert.assertNotNull("Name is set", mapper.getName());
            Assert.assertEquals("Name is right", "Name Mapper", mapper.getName());
            Assert.assertNotNull("Input is set", mapper.getInput());
            Assert.assertNotNull("Input schema is set", mapper.getInputSchema());
            Assert.assertNotNull("Output is set", mapper.getOutput());
            Assert.assertFalse("Output not a null pipe", mapper.getOutput() instanceof NullOutputPipe);
            Assert.assertNotNull("Mapping is set", mapper.getMapping());
            Assert.assertNotNull("Output schema is set", mapper.getOutputSchema());
            Assert.assertTrue("Mapping is by column definition",
                    mapper.getMapping().getMappingStrategy() instanceof ColumnNameMappingStrategy);
        }
    }
}

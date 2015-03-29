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

import com.surgingsystems.etl.filter.TransformerFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TransformerFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parse() {
        yamlParser.parse("transformer-test.yml");

        TransformerFilter transformer = applicationContext.getBean("transformer", TransformerFilter.class);
        Assert.assertNotNull("Found the bean", transformer);
        transformer.validate();

        Assert.assertNotNull("Name is set", transformer.getName());
        Assert.assertEquals("Name is right", "Transformer", transformer.getName());
        Assert.assertNotNull("Variables set", transformer.getVariables());
        Assert.assertTrue("Variables exist", transformer.getVariables().size() > 0);
        Assert.assertNotNull("Expressions set", transformer.getExpressions());
        Assert.assertTrue("Expressions exist", transformer.getExpressions().size() > 0);
        Assert.assertNotNull("Input is set", transformer.getInput());
        Assert.assertTrue("Outputs exists", transformer.getOutputConfigurations().size() > 0);
    }

}

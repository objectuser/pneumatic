package com.surgingsystems.etl.yamldsl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.Filter;
import com.surgingsystems.etl.pipe.Pipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class YamlParserTest {

    @Autowired
    private GenericApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parseTransformer() throws Exception {
        yamlParser.parse("transformer.yml");

        Map<String, Pipe> pipes = applicationContext.getBeansOfType(Pipe.class);
        Assert.assertEquals("Have pipes", 4, pipes.size());
        Map<String, Filter> filters = applicationContext.getBeansOfType(Filter.class);
        Assert.assertEquals("Have filters", 1, filters.size());
    }
}
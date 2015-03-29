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

import com.surgingsystems.etl.filter.RestfulListenerFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RestfulListenerFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parse() {
        yamlParser.parse("restful-listener-test.yml");

        RestfulListenerFilter restfulListener = applicationContext.getBean("restfulListener",
                RestfulListenerFilter.class);
        Assert.assertNotNull("Found the bean", restfulListener);
        restfulListener.validate();

        Assert.assertNotNull("Name is set", restfulListener.getName());
        Assert.assertEquals("Name is right", "Restful Listener", restfulListener.getName());
        Assert.assertEquals("Path is right", "mtb", restfulListener.getPath());
        Assert.assertNotNull("Output is set", restfulListener.getOutput());
        Assert.assertNotNull("Output schema is set", restfulListener.getOutputSchema());
    }

}

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

import com.surgingsystems.etl.filter.FunnelFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "funnel-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FunnelBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        FunnelFilter funnel = applicationContext.getBean("funnel", FunnelFilter.class);
        Assert.assertNotNull("Found the bean", funnel);
        funnel.validate();

        Assert.assertNotNull("Name is set", funnel.getName());
        Assert.assertEquals("Name is right", "Test Funnel", funnel.getName());
        Assert.assertTrue("Inputs exist", funnel.getInputs().size() > 0);
        Assert.assertNotNull("Output is set", funnel.getOutput());
        Assert.assertFalse("Output not a null pipe", funnel.getOutput() instanceof NullOutputPipe);
    }

}

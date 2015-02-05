package com.surgingsystems.etl.dsl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.filter.JoinFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "join-left-outer-test.xml" })
public class JoinLeftOuterBeanDefinitionParserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void parseFile() {
        JoinFilter join = applicationContext.getBean("join", JoinFilter.class);
        Assert.assertNotNull("Found the bean", join);
        join.validate();

        Assert.assertNotNull("Name is set", join.getName());
        Assert.assertEquals("Name is right", "Test Join", join.getName());
        Assert.assertNotNull("Left input is set", join.getLeftInput());
        Assert.assertNotNull("Right input is set", join.getRightInput());
        Assert.assertNotNull("Output is set", join.getOutput());
        Assert.assertFalse("Output not a null pipe", join.getOutput() instanceof NullOutputPipe);
        Assert.assertNotNull("Comparator is set", join.getComparator());
        Assert.assertNotNull("Schema is set", join.getOutputSchema());
        Assert.assertNotNull("Is left outer joni", join.isLeftOuterJoin());
    }

}

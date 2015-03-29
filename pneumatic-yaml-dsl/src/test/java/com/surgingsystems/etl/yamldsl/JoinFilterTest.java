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

import com.surgingsystems.etl.filter.JoinFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JoinFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void parse() {
        yamlParser.parse("join-test.yml");

        {
            JoinFilter join = applicationContext.getBean("innerJoin", JoinFilter.class);
            Assert.assertNotNull("Found the bean", join);
            join.validate();

            Assert.assertNotNull("Name is set", join.getName());
            Assert.assertEquals("Name is right", "Inner Join", join.getName());
            Assert.assertNotNull("Left input is set", join.getLeftInput());
            Assert.assertNotNull("Right input is set", join.getRightInput());
            Assert.assertNotNull("Output is set", join.getOutput());
            Assert.assertFalse("Output not a null pipe", join.getOutput() instanceof NullOutputPipe);
            Assert.assertNotNull("Comparator is set", join.getComparator());
            Assert.assertNotNull("Schema is set", join.getOutputSchema());
            Assert.assertFalse("Join strategy is inner", join.isLeftOuterJoin());
        }

        {
            JoinFilter join = applicationContext.getBean("leftOuterJoin", JoinFilter.class);
            Assert.assertNotNull("Found the bean", join);
            join.validate();

            Assert.assertNotNull("Name is set", join.getName());
            Assert.assertEquals("Name is right", "Left Outer Join", join.getName());
            Assert.assertNotNull("Left input is set", join.getLeftInput());
            Assert.assertNotNull("Right input is set", join.getRightInput());
            Assert.assertNotNull("Output is set", join.getOutput());
            Assert.assertFalse("Output not a null pipe", join.getOutput() instanceof NullOutputPipe);
            Assert.assertNotNull("Comparator is set", join.getComparator());
            Assert.assertNotNull("Schema is set", join.getOutputSchema());
            Assert.assertTrue("Join strategy is left outer", join.isLeftOuterJoin());
        }

    }

}

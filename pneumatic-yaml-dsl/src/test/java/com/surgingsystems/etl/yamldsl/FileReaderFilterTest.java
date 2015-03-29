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

import com.surgingsystems.etl.filter.FileReaderFilter;
import com.surgingsystems.etl.pipe.NullOutputPipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FileReaderFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void read() {
        yamlParser.parse("file-reader-test.yml");

        {
            FileReaderFilter fileReaderFilter = applicationContext.getBean("fileReader1", FileReaderFilter.class);
            Assert.assertNotNull("Found the bean", fileReaderFilter);
            fileReaderFilter.validate();

            Assert.assertNotNull("Name is set", fileReaderFilter.getName());
            Assert.assertEquals("Name is right", "Test File Reader", fileReaderFilter.getName());
            Assert.assertNotNull("Output is set", fileReaderFilter.getOutput());
            Assert.assertFalse("Output not a null pipe", fileReaderFilter.getOutput() instanceof NullOutputPipe);
            Assert.assertNotNull("File resource is set", fileReaderFilter.getItemReader());
        }

        {
            FileReaderFilter fileReaderFilter = applicationContext.getBean("fileReader2", FileReaderFilter.class);
            Assert.assertNotNull("Found the bean", fileReaderFilter);
            fileReaderFilter.validate();

            Assert.assertNotNull("Name is set", fileReaderFilter.getName());
            Assert.assertEquals("Name is right", "Test File Reader", fileReaderFilter.getName());
            Assert.assertNotNull("Output is set", fileReaderFilter.getOutput());
            Assert.assertFalse("Output not a null pipe", fileReaderFilter.getOutput() instanceof NullOutputPipe);
            Assert.assertNotNull("File resource is set", fileReaderFilter.getItemReader());
        }
    }
}

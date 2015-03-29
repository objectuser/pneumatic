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

import com.surgingsystems.etl.filter.FileWriterFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FileWriterFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YamlParser yamlParser;

    @Test
    public void read() {
        yamlParser.parse("file-writer-test.yml");

        FileWriterFilter fileWriterFilter = applicationContext.getBean("fileWriter", FileWriterFilter.class);
        Assert.assertNotNull("Found the bean", fileWriterFilter);
        fileWriterFilter.validate();

        Assert.assertNotNull("Name is set", fileWriterFilter.getName());
        Assert.assertEquals("Name is right", "File Writer", fileWriterFilter.getName());
        Assert.assertNotNull("Input is set", fileWriterFilter.getInput());
        Assert.assertNotNull("File resource is set", fileWriterFilter.getFileResource());
        Assert.assertNotNull("Schema is set", fileWriterFilter.getInputSchema());
    }

}

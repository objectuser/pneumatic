package com.surgingsystems.etltest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.XmlRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:configs/file-with-job-arguments-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReadFileWithArgumentsTest {

    @Test
    public void write() throws Exception {
        XmlRunner.main(new String[] { "-Dfile=classpath:data/input1.txt", "classpath:configs/file-test.xml" });
    }
}

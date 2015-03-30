package com.surgingsystems.etltest;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.Job;
import com.surgingsystems.etl.JobConfigurer;
import com.surgingsystems.etl.XmlJobConfigurer;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.pipe.PipeUtility;
import com.surgingsystems.etl.record.Record;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:configs/database-lookup-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseLookupTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;
    
    @Resource(name = "copyOutputForTest")
    private Pipe testPipe;

    @Test
    public void write() throws Exception {
        JobConfigurer configurer = new XmlJobConfigurer(applicationContext);
        Job job = configurer.buildJob();
        job.start();
        
        List<Record> outputRecords = PipeUtility.toList(testPipe);
        Assert.assertEquals("Have all the records", 6, outputRecords.size());
        for (Record record : outputRecords) {
            int year = record.getValueForName("year");
            Assert.assertEquals("Year is right",  2015, year);
        }
    }
}

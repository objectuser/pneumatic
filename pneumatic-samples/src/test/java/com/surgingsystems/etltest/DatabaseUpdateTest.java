package com.surgingsystems.etltest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.Job;
import com.surgingsystems.etl.JobConfigurer;
import com.surgingsystems.etl.XmlJobConfigurer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "classpath:configs/database-update-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseUpdateTest {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private DataSource dataSource;

    @Test
    public void write() {
        JobConfigurer configurer = new XmlJobConfigurer(applicationContext);
        Job job = configurer.buildJob();
        job.start();
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<String> years = jdbcTemplate.query("select year from mtb", new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("year");
            }
        });
        
        Assert.assertEquals(3, years.size());
        years.retainAll(Arrays.asList("2015"));
        Assert.assertEquals(3, years.size());
    }
}

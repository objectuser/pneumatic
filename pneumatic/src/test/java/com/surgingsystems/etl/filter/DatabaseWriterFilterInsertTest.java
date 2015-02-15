package com.surgingsystems.etl.filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "database-writer-filter-insert-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseWriterFilterInsertTest {

    @Autowired
    private DatabaseWriterFilter databaseWriterFilter;

    @Resource(name = "fileReaderOutput")
    private Pipe input;

    @Resource(name = "mtbSchema")
    private Schema inputSchema;

    @Autowired
    private DataSource dataSource;

    @Test(expected = IllegalArgumentException.class)
    public void outputPipeIsRequired() {
        databaseWriterFilter.setInput(null);
        databaseWriterFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void dataSourceIsRequired() {
        databaseWriterFilter.setDataSource(null);
        databaseWriterFilter.validate();
    }

    @Test
    public void insert() {

        DataRecord trance = new DataRecord(inputSchema, "Giant Trance", 2016, 4000.0);
        DataRecord bronson = new DataRecord(inputSchema, "Santa Cruz Bronson", 2016, 5000.0);
        DataRecord mojo = new DataRecord(inputSchema, "Ibis Mojo", 2016, 6000.0);
        input.put(trance);
        input.put(bronson);
        input.put(mojo);
        input.closedForInput();

        databaseWriterFilter.run();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Record> records = jdbcTemplate.query("select * from mtb where year = 2016", new RowMapper<Record>() {

            @Override
            public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DataRecord(inputSchema, rs.getString("name"), rs.getInt("year"), rs.getDouble("cost"));
            }
        });

        Assert.assertEquals("3 elements", 3, records.size());
        Assert.assertTrue("Has Trance", records.contains(trance));
        Assert.assertTrue("Has Trance", records.contains(bronson));
        Assert.assertTrue("Has Trance", records.contains(mojo));
    }
}

package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class ReadFromDatabaseTest {

    @Test
    public void write() throws Exception {
        XmlRunner.main(new String[] { "configs/database-reader-test.xml" });
    }
}

package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class CopyFileTest {

    @Test
    public void write() throws Exception {
        XmlRunner.main(new String[] { "configs/performance/copy-job.xml" });
    }
}

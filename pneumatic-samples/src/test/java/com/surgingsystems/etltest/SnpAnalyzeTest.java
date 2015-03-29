package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class SnpAnalyzeTest {

    @Test
    public void write() throws Exception {
        XmlRunner.main(new String[] { "configs/snp-analyze-test.xml" });
    }
}

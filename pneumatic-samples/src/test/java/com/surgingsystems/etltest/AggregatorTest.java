package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class AggregatorTest {

    @Test
    public void write() throws Exception {
        XmlRunner.main(new String[] { "configs/aggregator-test.xml" });
    }

}

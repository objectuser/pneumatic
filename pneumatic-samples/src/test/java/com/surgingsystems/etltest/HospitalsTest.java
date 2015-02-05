package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class HospitalsTest {

    @Test
    public void sort() throws Exception {
        XmlRunner.main(new String[] { "configs/hospitals-test.xml" });
    }
}

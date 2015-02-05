package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class ModifierFileTest {

    @Test
    public void write() throws Exception {
        XmlRunner.main(new String[] { "configs/modifier-test.xml" });
    }
}

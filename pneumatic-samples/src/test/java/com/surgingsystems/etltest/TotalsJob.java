package com.surgingsystems.etltest;

import org.junit.Test;

import com.surgingsystems.etl.XmlRunner;

public class TotalsJob {

    @Test
    public void run() throws Exception {
        XmlRunner.main(new String[] { "configs/totals-job.xml" });
    }
}

package com.surgingsystems.etl.samples;

import com.surgingsystems.etl.BootRunner;

public class RestfulListenerJob {

    public void write() throws Exception {
        BootRunner.main(new String[] { "configs/restful-listener-job.xml" });
    }
}

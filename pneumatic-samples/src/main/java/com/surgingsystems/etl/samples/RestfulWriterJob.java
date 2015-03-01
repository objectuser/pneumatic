package com.surgingsystems.etl.samples;

import com.surgingsystems.etl.XmlRunner;

public class RestfulWriterJob {

    public void write() throws Exception {
        XmlRunner.main(new String[] { "configs/restful-writer-job.xml" });
    }
}

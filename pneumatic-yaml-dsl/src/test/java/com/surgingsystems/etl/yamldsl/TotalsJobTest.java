package com.surgingsystems.etl.yamldsl;

import com.surgingsystems.etl.YamlRunner;

public class TotalsJobTest {

    public static void main(String[] args) throws Exception {
        YamlRunner.main(new String[] { "configs/totals-job.yml" });
    }

}

package com.surgingsystems.etl.samples;

import com.surgingsystems.etl.XmlRunner;

/**
 * Before running any of these samples, you may want to setup your own database.
 * By default these samples use HSQL, which makes them run but probably doesn't
 * provide a lot of useful information.
 * 
 * Run the insert test first to populate a database with test data.
 * 
 * Next run dump to get a baseline for reading all the data in the table and
 * writing it to a file.
 * 
 * Finally, run the partition test and compare it to the time for the dump test.
 * If it's faster, why? If it's not, why not?
 */
public class PartitionJob {

    public static void main(String[] args) throws Exception {
        switch (args[0]) {
        case "insert":
            insert();
            break;
        case "dump":
            dump();
            break;
        case "partition":
            partition();
            break;
        }
    }

    public static void insert() throws Exception {
        XmlRunner.main(new String[] { "configs/performance/insert-hospital-records.xml" });
    }

    public static void dump() throws Exception {
        XmlRunner.main(new String[] { "configs/performance/dump.xml" });
    }

    public static void partition() throws Exception {
        XmlRunner.main(new String[] { "configs/performance/partitioning.xml" });
    }
}

package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.record.Record;

/**
 * Configurable behavior for rejected records.
 */
public interface RejectRecordStrategy {

    /**
     * The <em>input</em> record that resulted in the rejection.
     */
    void rejected(Record record);

    void close();
}

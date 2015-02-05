package com.surgingsystems.etl.schema;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;

/**
 * A validator that always accepts the record.
 */
public class AcceptingRecordValidator implements RecordValidator {

    @Override
    public boolean accepts(Record record) {
        return true;
    }

}

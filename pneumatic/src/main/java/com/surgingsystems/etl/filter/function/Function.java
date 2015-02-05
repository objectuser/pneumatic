package com.surgingsystems.etl.filter.function;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public interface Function<Result extends Comparable<Result>> {
    
    void apply(Record record);

    Column<?> getResult();

    ColumnDefinition<Result> getOutputColumnDefinition();

    boolean containsRequiredInput(Schema inputSchema);

    boolean containsRequiredOutput(Schema aggregatorOutputSchema);
}

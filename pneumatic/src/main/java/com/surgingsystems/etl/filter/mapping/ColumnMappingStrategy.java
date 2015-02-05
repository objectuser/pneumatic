package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public interface ColumnMappingStrategy {

    <T extends Comparable<T>> Column<T> mapColumn(Record input, ColumnDefinition<T> toColumnDefinition);

    void validate(Schema inputSchema, Schema outputSchema);
}

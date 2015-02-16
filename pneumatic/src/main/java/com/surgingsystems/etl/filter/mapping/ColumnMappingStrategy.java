package com.surgingsystems.etl.filter.mapping;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public interface ColumnMappingStrategy {

    Column<? extends Comparable<?>> mapColumn(Record input, ColumnDefinition<? extends Comparable<?>> toColumnDefinition);

    void validate(Schema outputSchema, Schema... inputSchemas);
}

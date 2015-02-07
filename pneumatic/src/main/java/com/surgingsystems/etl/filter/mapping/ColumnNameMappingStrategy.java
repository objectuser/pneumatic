package com.surgingsystems.etl.filter.mapping;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class ColumnNameMappingStrategy implements ColumnMappingStrategy {

    @Override
    public <T extends Comparable<T>> Column<T> mapColumn(Record input, ColumnDefinition<T> toColumnDefinition) {
        if (input.hasColumnFor(toColumnDefinition)) {
            return input.getColumnForName(toColumnDefinition.getName());
        } else {
            return null;
        }
    }

    @Override
    public void validate(Schema outputSchema, Schema... inputSchemas) {
        Set<ColumnDefinition<?>> inputColumns = new HashSet<ColumnDefinition<?>>();
        for (Schema inputSchema : inputSchemas) {
            inputColumns.addAll(inputSchema.getColumnDefinitions());
        }

        Set<String> outputColumnNames = new HashSet<String>();
        for (ColumnDefinition<?> columnDefinition : outputSchema) {
            outputColumnNames.add(columnDefinition.getName());
        }

        Set<String> inputColumnNames = new HashSet<String>();
        for (ColumnDefinition<?> columnDefinition : inputColumns) {
            inputColumnNames.add(columnDefinition.getName());
        }

        Assert.isTrue(inputColumnNames.containsAll(outputColumnNames),
                "The input schema must have columns matching the names of those in the output schema");
    }
}

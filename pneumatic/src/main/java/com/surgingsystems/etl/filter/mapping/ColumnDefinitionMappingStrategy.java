package com.surgingsystems.etl.filter.mapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class ColumnDefinitionMappingStrategy implements ColumnMappingStrategy {

    private Map<ColumnDefinition<?>, ColumnDefinition<?>> outputColumnDefinitionToInputColumnDefinitionMap;

    public ColumnDefinitionMappingStrategy(Map<ColumnDefinition<?>, ColumnDefinition<?>> mappings) {
        this.outputColumnDefinitionToInputColumnDefinitionMap = mappings;
    }

    @Override
    public Column<? extends Comparable<?>> mapColumn(Record input,
            ColumnDefinition<? extends Comparable<?>> toColumnDefinition) {
        ColumnDefinition<?> inputColumnDefinition = outputColumnDefinitionToInputColumnDefinitionMap
                .get(toColumnDefinition);
        return input.getColumnFor(inputColumnDefinition);
    }

    @Override
    public void validate(Schema outputSchema, Schema... inputSchemas) {
        Set<ColumnDefinition<?>> inputColumns = new HashSet<ColumnDefinition<?>>();
        for (Schema inputSchema : inputSchemas) {
            inputColumns.addAll(inputSchema.getColumns());
        }

        for (Map.Entry<ColumnDefinition<?>, ColumnDefinition<?>> entry : outputColumnDefinitionToInputColumnDefinitionMap
                .entrySet()) {
            ColumnDefinition<?> outputColumn = entry.getKey();
            Assert.isTrue(
                    outputSchema.contains(outputColumn),
                    String.format("The output schema must contain a definition for column named %s",
                            outputColumn.getName()));

            ColumnDefinition<?> inputColumn = entry.getValue();
            Assert.isTrue(
                    inputColumns.contains(inputColumn),
                    String.format("The input schema must contain a definition for column named %s",
                            inputColumn.getName()));
        }

        for (ColumnDefinition<?> outputSchemaColumnDefinition : outputSchema) {
            Assert.isTrue(outputColumnDefinitionToInputColumnDefinitionMap.containsKey(outputSchemaColumnDefinition),
                    String.format("The column mapping must contain a mapping to column named %s",
                            outputSchemaColumnDefinition.getName()));
        }
    }
}

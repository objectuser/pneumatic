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
        return input.getColumnForName(toColumnDefinition.getName());
    }

    @Override
    public void validate(Schema inputSchema, Schema outputSchema) {
        
      Set<String> outputColumnNames = new HashSet<String>();
      for (ColumnDefinition<?> columnDefinition : outputSchema) {
          outputColumnNames.add(columnDefinition.getName());
      }

      Set<String> inputColumnNames = new HashSet<String>();
      for (ColumnDefinition<?> columnDefinition : inputSchema) {
          inputColumnNames.add(columnDefinition.getName());
      }

      Assert.isTrue(inputColumnNames.containsAll(outputColumnNames),
              "The input schema must have columns matching the names of those in the output schema");        
    }
}

package com.surgingsystems.etl.filter.mapping;

import org.junit.Before;
import org.junit.Test;

import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.StringColumnType;
import com.surgingsystems.etl.schema.TabularSchema;

public class ColumnNameMappingStrategyTest {
    
    private ColumnNameMappingStrategy columnNameMappingStrategy = new ColumnNameMappingStrategy();

    private Schema inputSchema;

    private Schema outputSchema;

    private Schema mismatchOutputSchema;

    @Before
    public void setupSchema() {
        inputSchema = new TabularSchema("Test Schema", new ColumnDefinition<String>("Name", new StringColumnType()),
                new ColumnDefinition<String>("Count", new StringColumnType()), new ColumnDefinition<String>("Price",
                        new StringColumnType()));
        
        outputSchema = new TabularSchema("Test Schema", new ColumnDefinition<String>("Name", new StringColumnType()),
                new ColumnDefinition<Integer>("Count", new IntegerColumnType()), new ColumnDefinition<Double>("Price",
                        new DecimalColumnType()));
        
        mismatchOutputSchema = new TabularSchema("Test Schema", new ColumnDefinition<String>("Name", new StringColumnType()),
                new ColumnDefinition<Integer>("Count", new IntegerColumnType()), new ColumnDefinition<Double>("Cost",
                        new DecimalColumnType()));
    }

    @Test
    public void validateCompatibleSchemas() {
        columnNameMappingStrategy.validate(outputSchema, inputSchema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIncompatibleSchemas() {
        columnNameMappingStrategy.validate(mismatchOutputSchema, inputSchema);
    }
}

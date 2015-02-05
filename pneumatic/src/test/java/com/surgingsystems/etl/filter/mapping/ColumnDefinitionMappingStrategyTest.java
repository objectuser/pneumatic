package com.surgingsystems.etl.filter.mapping;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.StringColumnType;
import com.surgingsystems.etl.schema.TabularSchema;

public class ColumnDefinitionMappingStrategyTest {
    
    private ColumnDefinitionMappingStrategy columnDefinitionMappingStrategy;

    private Schema inputSchema;

    private Schema outputSchema;

    private Schema mismatchOutputSchema;

    private ColumnDefinition<String> name;

    private ColumnDefinition<String> count;

    private ColumnDefinition<String> price;

    private ColumnDefinition<String> bikeName;

    private ColumnDefinition<Integer> itemCount;

    private ColumnDefinition<Double> itemPrice;

    @Before
    public void setupSchema() {
        name = new ColumnDefinition<String>("Name", new StringColumnType());
        count = new ColumnDefinition<String>("Count", new StringColumnType());
        price = new ColumnDefinition<String>("Price", new StringColumnType());

        inputSchema = new TabularSchema("Test Schema", name, count, price);

        bikeName = new ColumnDefinition<String>("Bike Name", new StringColumnType());
        itemCount = new ColumnDefinition<Integer>("Item Count", new IntegerColumnType());
        itemPrice = new ColumnDefinition<Double>("Item Price", new DecimalColumnType());

        outputSchema = new TabularSchema("Test Schema", bikeName, itemCount, itemPrice);
        
        Map<ColumnDefinition<?>, ColumnDefinition<?>> mappings = new HashMap<ColumnDefinition<?>, ColumnDefinition<?>>();
        mappings.put(bikeName, name);
        mappings.put(itemCount, count);
        mappings.put(itemPrice, price);

        ColumnDefinition<Double> salePrice = new ColumnDefinition<Double>("Sale Price", new DecimalColumnType());

        mismatchOutputSchema = new TabularSchema("Test Schema", bikeName, itemCount, itemPrice, salePrice);
        
        columnDefinitionMappingStrategy = new ColumnDefinitionMappingStrategy(mappings);
    }

    @Test
    public void validateCompatibleSchemas() {
        columnDefinitionMappingStrategy.validate(inputSchema, outputSchema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIncompatibleSchemas() {
        columnDefinitionMappingStrategy.validate(inputSchema, mismatchOutputSchema);
    }
    
    @Test
    public void mapStringToString() {
        Record record = new DataRecord(inputSchema, "Trance", 100, 3000.0);
        Column<String> nameColumn = columnDefinitionMappingStrategy.mapColumn(record, bikeName);
        Assert.assertNotNull("Column mapped", nameColumn);
    }
    
    @Test
    public void mapStringToInteger() {
        Record record = new DataRecord(inputSchema, "Trance", 100, 3000.0);
        Column<Integer> itemCountColumn = columnDefinitionMappingStrategy.mapColumn(record, itemCount);
        Assert.assertNotNull("Column mapped", itemCountColumn);
        Assert.assertEquals("Value mapped", 100, (int) itemCountColumn.getValue());
    }
    
    @Test
    public void mapStringToDecimal() {
        Record record = new DataRecord(inputSchema, "Trance", 100, 3000.0);
        Column<Double> itemPriceColumn = columnDefinitionMappingStrategy.mapColumn(record, itemPrice);
        Assert.assertNotNull("Column mapped", itemPriceColumn);
        Assert.assertEquals("Value mapped", 3000.0, (double) itemPriceColumn.getValue(), 0.0);
    }
}

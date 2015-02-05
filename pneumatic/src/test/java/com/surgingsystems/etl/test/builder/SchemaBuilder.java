package com.surgingsystems.etl.test.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.ColumnType;
import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.StringColumnType;
import com.surgingsystems.etl.schema.TabularSchema;

public class SchemaBuilder {
    
    @Autowired
    private StringColumnType stringColumnType;
    
    @Autowired
    private IntegerColumnType integerColumnType;
    
    @Autowired
    private DecimalColumnType decimalColumnType;
    
    public static final String NAME_COLUMN = "Name";
    
    public static final String INTEGER_COLUMN = "An Integer";
    
    public static final String DECIMAL_COLUMN = "A Decimal";

    private String name = "Test Schema";

    private List<ColumnDefinition<? extends Comparable<?>>> columnDefinitions = new ArrayList<ColumnDefinition<? extends Comparable<?>>>();
    
    public SchemaBuilder() { }
    
    public static SchemaBuilder create() {
        return new SchemaBuilder();
    }
    
    @SuppressWarnings("serial")
    public SchemaBuilder withDefaultDefinitions() {
        setColumns(new HashMap<String, ColumnType<? extends Comparable<?>>>() {{
            put(NAME_COLUMN, stringColumnType);
            put(INTEGER_COLUMN, integerColumnType);
            put(DECIMAL_COLUMN, decimalColumnType);
        }});
        return this;
    }
    
    public SchemaBuilder withIntegerColumn(String name) {
        columnDefinitions.add(new ColumnDefinition<Integer>(name, integerColumnType));
        return this;
    }
    
    public SchemaBuilder withDecimalColumn(String name) {
        columnDefinitions.add(new ColumnDefinition<Double>(name, decimalColumnType));
        return this;
    }

    public Schema build() {
        return new TabularSchema(name, columnDefinitions);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setColumns(Map<String, ColumnType<? extends Comparable<?>>> columns) {
        for (Map.Entry<String, ColumnType<? extends Comparable<?>>> entry : columns.entrySet()) {
            columnDefinitions.add(new ColumnDefinition(entry.getKey(), entry.getValue()));
        }
    }
}

package com.surgingsystems.etl.filter.mapping;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

/**
 * Map input records to output records based on configuration.
 */
public class Mapping {

    private static Logger logger = LogManager.getFormatterLogger(Mapping.class);

    private ColumnMappingStrategy mappingStrategy = new ColumnNameMappingStrategy();

    private ColumnMismatchStrategy mismatchStrategy = new NullColumnMismatchStrategy();

    private Schema outputSchema;

    public Mapping() {
    }

    public Mapping(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    /**
     * Do the schemas conform to the mapping strategy?
     */
    public void validate(Schema inputSchema) {
        mappingStrategy.validate(inputSchema, outputSchema);
    }

    /**
     * Use the input records to create a single output record.
     * 
     * @return The mapped record or null if the record was rejected.
     */
    public Record map(Record... inputRecords) {
        Record result = outputSchema.createEmptyRecord();
        for (ColumnDefinition<?> toColumnDefinition : outputSchema) {
            for (Record input : inputRecords) {
                Column<?> inputColumn = mappingStrategy.mapColumn(input, toColumnDefinition);
                if (inputColumn != null) {
                    if (toColumnDefinition.accepts(inputColumn)) {
                        result.setColumn(toColumnDefinition.applyTo(inputColumn));
                    } else {
                        logger.debug("Column (%s) not compatible with defintion (%s), applying mismatch strategy",
                                inputColumn.getName(), toColumnDefinition.getName());
                        boolean reject = mismatchStrategy.applyTo(result, inputColumn.getValue(), toColumnDefinition);
                        if (reject) {
                            return null;
                        }
                    }
                }
            }
        }

        return result;
    }

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    public void setColumnDefinitionMappings(Map<ColumnDefinition<?>, ColumnDefinition<?>> mappings) {
        mappingStrategy = new ColumnDefinitionMappingStrategy(mappings);
    }

    public ColumnMappingStrategy getMappingStrategy() {
        return mappingStrategy;
    }

    public void setMappingStrategy(ColumnMappingStrategy mappingStrategy) {
        this.mappingStrategy = mappingStrategy;
    }

    public ColumnMismatchStrategy getMismatchStrategy() {
        return mismatchStrategy;
    }

    public void setMismatchStrategy(ColumnMismatchStrategy mismatchStrategy) {
        this.mismatchStrategy = mismatchStrategy;
    }
}

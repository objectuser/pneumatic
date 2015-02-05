package com.surgingsystems.etl.filter.function;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.Schema;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AverageFunction implements Function<Double> {

    private DecimalColumnType decimalColumnType;

    private ColumnDefinition<?> inputColumnDefinition;

    private ColumnDefinition<Double> outputColumnDefinition;

    private Double result = new Double(0.0);

    private long recordCount = 0L;

    @Override
    public void apply(Record record) {
        Object value = record.getValueFor(inputColumnDefinition);
        if (value != null && decimalColumnType.isCompatible(value)) {
            Double doubleValue = decimalColumnType.convert(value);
            result += doubleValue;
            ++recordCount;
        }
    }

    @Override
    public Column<Double> getResult() {
        Double average = result / recordCount;
        return new Column<Double>(outputColumnDefinition, average);
    }

    public ColumnDefinition<?> getInputColumnDefinition() {
        return inputColumnDefinition;
    }

    public void setInputColumnDefinition(ColumnDefinition<?> inputColumnDefinition) {
        this.inputColumnDefinition = inputColumnDefinition;
    }

    @Override
    public ColumnDefinition<Double> getOutputColumnDefinition() {
        return outputColumnDefinition;
    }

    public void setOutputColumnDefinition(ColumnDefinition<Double> outputColumnDefinition) {
        this.outputColumnDefinition = outputColumnDefinition;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setDecimalColumnType(DecimalColumnType decimalColumnType) {
        this.decimalColumnType = decimalColumnType;
    }

    @Override
    public boolean containsRequiredInput(Schema inputSchema) {
        return inputSchema != null && inputSchema.contains(inputColumnDefinition);
    }

    @Override
    public boolean containsRequiredOutput(Schema aggregatorOutputSchema) {
        return aggregatorOutputSchema.contains(outputColumnDefinition);
    }
}

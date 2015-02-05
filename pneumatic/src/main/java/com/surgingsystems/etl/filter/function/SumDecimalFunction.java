package com.surgingsystems.etl.filter.function;

import org.springframework.beans.factory.annotation.Autowired;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.DecimalColumnType;

public class SumDecimalFunction extends SumFunction<Double> {

    @Autowired
    private DecimalColumnType decimalColumnType;

    private Double result = 0.0;

    @Override
    public void apply(Record record) {
        Object value = record.getValueFor(getInputColumnDefinition());
        if (value != null && decimalColumnType.isCompatible(value)) {
            Double doubleValue = decimalColumnType.convert(value);
            result += doubleValue;
        }
    }

    @Override
    public Column<Double> getResult() {
        return new Column<Double>(getOutputColumnDefinition(), result);
    }
}

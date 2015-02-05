package com.surgingsystems.etl.filter.function;

import org.springframework.beans.factory.annotation.Autowired;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.IntegerColumnType;

public class SumIntegerFunction extends SumFunction<Integer> {

    @Autowired
    private IntegerColumnType integerColumnType;

    private int result = 0;

    @Override
    public void apply(Record record) {
        Object value = record.getValueFor(getInputColumnDefinition());
        if (value != null && integerColumnType.isCompatible(value)) {
            int intValue = integerColumnType.convert(value);
            result += intValue;
        }
    }

    @Override
    public Column<Integer> getResult() {
        return new Column<Integer>(getOutputColumnDefinition(), result);
    }
}

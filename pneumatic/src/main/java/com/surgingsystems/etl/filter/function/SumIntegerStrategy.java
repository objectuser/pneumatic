package com.surgingsystems.etl.filter.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.surgingsystems.etl.schema.IntegerColumnType;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SumIntegerStrategy implements SumStrategy<Integer> {

    @Autowired
    private IntegerColumnType integerColumnType;

    private int result = 0;

    @Override
    public void add(Object value) {
        if (value != null && integerColumnType.isCompatible(value)) {
            int intValue = integerColumnType.convert(value);
            result += intValue;
        }
    }

    public IntegerColumnType getIntegerColumnType() {
        return integerColumnType;
    }

    public void setIntegerColumnType(IntegerColumnType integerColumnType) {
        this.integerColumnType = integerColumnType;
    }

    @Override
    public Integer getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}

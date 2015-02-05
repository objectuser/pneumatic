package com.surgingsystems.etl.filter.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.surgingsystems.etl.schema.DecimalColumnType;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SumDecimalStrategy implements SumStrategy<Double> {

    @Autowired
    private DecimalColumnType decimalColumnType;

    private Double result = 0.0;

    @Override
    public void add(Object value) {
        if (value != null && decimalColumnType.isCompatible(value)) {
            Double decimalValue = decimalColumnType.convert(value);
            result += decimalValue;
        }
    }

    public DecimalColumnType getDecimalColumnType() {
        return decimalColumnType;
    }

    public void setDecimalColumnType(DecimalColumnType decimalColumnType) {
        this.decimalColumnType = decimalColumnType;
    }

    @Override
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

}

package com.surgingsystems.etl.filter.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.Schema;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CounterFunction implements Function<Integer> {

    @Autowired
    private IntegerColumnType integerColumnType;

    private ColumnDefinition<Integer> outputColumnDefinition;

    private int count = 0;

    @Override
    public void apply(Record record) {
        ++count;
    }

    @Override
    public Column<Integer> getResult() {
        return new Column<Integer>(outputColumnDefinition, count);
    }

    @Override
    public ColumnDefinition<Integer> getOutputColumnDefinition() {
        return outputColumnDefinition;
    }

    public void setOutputColumnDefinition(ColumnDefinition<Integer> outputColumnDefinition) {
        this.outputColumnDefinition = outputColumnDefinition;
    }

    public void setIntegerColumnType(IntegerColumnType integerColumnType) {
        this.integerColumnType = integerColumnType;
    }

    @Override
    public boolean containsRequiredInput(Schema inputSchema) {
        return true;
    }

    @Override
    public boolean containsRequiredOutput(Schema aggregatorOutputSchema) {
        return aggregatorOutputSchema.contains(outputColumnDefinition);
    }
}

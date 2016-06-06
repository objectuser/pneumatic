package com.surgingsystems.etl.filter.function;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class SumFunction<T extends Comparable<T>> implements Function<T> {

    private static Logger logger = LogManager.getFormatterLogger(SumFunction.class);

    private ColumnDefinition<T> inputColumnDefinition;

    private ColumnDefinition<T> outputColumnDefinition;

    private SumStrategy<T> sumStrategy;

    @PostConstruct
    public void validate() {
        logger.trace("Sum Function: validating");
        Assert.notNull(getInputColumnDefinition(), "The 'in' column is required");
        Assert.notNull(getOutputColumnDefinition(), "The 'out' column is required");
        Assert.notNull(getSumStrategy(), "The summation strategy is not set");
    }

    @Override
    public void apply(Record record) {
        Object value = record.getValueFor(getInputColumnDefinition());
        sumStrategy.add(value);
    }

    @Override
    public Column<T> getResult() {
        return new Column<T>(getOutputColumnDefinition(), sumStrategy.getResult());
    }

    public void setInputColumnDefinition(ColumnDefinition<T> inputColumnDefinition) {
        this.inputColumnDefinition = inputColumnDefinition;
    }

    public void setOutputColumnDefinition(ColumnDefinition<T> outputColumnDefinition) {
        this.outputColumnDefinition = outputColumnDefinition;
    }

    public ColumnDefinition<?> getInputColumnDefinition() {
        return inputColumnDefinition;
    }

    @Override
    public ColumnDefinition<T> getOutputColumnDefinition() {
        return outputColumnDefinition;
    }

    public SumStrategy<T> getSumStrategy() {
        return sumStrategy;
    }

    public void setSumStrategy(SumStrategy<T> sumStrategy) {
        this.sumStrategy = sumStrategy;
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

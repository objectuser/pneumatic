package com.surgingsystems.etl.filter.function;

public interface SumStrategy<T> {

    void add(Object value);

    T getResult();
}

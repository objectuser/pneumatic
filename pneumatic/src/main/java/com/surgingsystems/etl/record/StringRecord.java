package com.surgingsystems.etl.record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.MethodNotFoundException;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.StringColumnType;

/**
 * Make an array of strings conform to the record interface. The implementation
 * is not complete.
 */
public class StringRecord implements Record {

    private List<Column<?>> columns;

    public StringRecord(StringColumnType columnType, String line) {
        columns = Arrays.asList(new Column<String>(new ColumnDefinition<String>("Line", columnType), line));
    }

    public StringRecord(StringColumnType columnType, String... strings) {
        columns = new ArrayList<Column<?>>();
        int i = 0;
        for (String string : strings) {
            columns.add(new Column<String>(new ColumnDefinition<String>("Value" + i, columnType), string));
        }
    }

    @Override
    public Iterator<Column<?>> iterator() {
        return columns.iterator();
    }

    @Override
    public <T extends Comparable<T>> T getValueFor(ColumnDefinition<T> columnDefinition) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public <T extends Comparable<T>> T getValueForName(String columnName) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public <T extends Comparable<T>> Column<T> getColumnFor(ColumnDefinition<T> columnDefinition) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public <T extends Comparable<T>> Column<T> getColumnForName(String columnName) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public List<Column<?>> getColumns() {
        return columns;
    }

    @Override
    public boolean hasColumnFor(ColumnDefinition<?> columnDefinition) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public boolean hasColumnForName(String columnName) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public void setColumn(Column<?> column) {
        throw new MethodNotFoundException("The method is not implemented");

    }

    @Override
    public Map<String, String> toMap() {
        throw new MethodNotFoundException("The method is not implemented");
    }

}

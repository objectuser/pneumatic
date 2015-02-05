package com.surgingsystems.etl.record;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.el.MethodNotFoundException;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.ConvertableColumnType;

public class ResultSetRecord implements Record {

    private ResultSet resultSet;

    public ResultSetRecord(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    @Override
    public Iterator<Column<?>> iterator() {
        return null;
    }

    @Override
    public <T extends Comparable<T>> T getValueFor(ColumnDefinition<T> columnDefinition) {
        try {
            return columnDefinition.applyToValue(resultSet.getObject(columnDefinition.getName())).getValue();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get value from result set", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Comparable<T>> T getValueForName(String columnName) {
        try {
            return (T) resultSet.getObject(columnName);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get value from result set", e);
        }
    }

    @Override
    public <T extends Comparable<T>> Column<T> getColumnFor(ColumnDefinition<T> columnDefinition) {
        try {
            return columnDefinition.applyToValue(resultSet.getObject(columnDefinition.getName()));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get value from result set", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Comparable<T>> Column<T> getColumnForName(String columnName) {
        try {
            return new Column<T>(new ColumnDefinition<T>(columnName, new NullColumnType<T>()), (T) resultSet.getObject(columnName));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get value from result set", e);
        }
    }

    @Override
    public List<Column<?>> getColumns() {
        throw new MethodNotFoundException("The method is not implemented");
    }

    @Override
    public boolean hasColumnFor(ColumnDefinition<?> columnDefinition) {
        throw new MethodNotFoundException("The method is not implemented");
    }
    
    @Override
    public void setColumn(Column<?> column) {
        throw new MethodNotFoundException("The method is not implemented");
    }
    
    private class NullColumnType<T extends Comparable<T>> extends ConvertableColumnType<T> {

        @SuppressWarnings("unchecked")
        @Override
        public Class<T> getCoreType() {
            return (Class<T>) Comparable.class;
        }

        @Override
        protected boolean isValueCompatible(String value) {
            return false;
        }
    }
}

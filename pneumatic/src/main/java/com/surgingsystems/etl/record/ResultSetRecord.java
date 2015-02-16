package com.surgingsystems.etl.record;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.el.MethodNotFoundException;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.ConvertableColumnType;

public class ResultSetRecord implements Record {

    private ResultSet resultSet;

    // These come from the metadata as upper case.
    private List<String> columnNames = new ArrayList<String>();

    public ResultSetRecord(ResultSet resultSet) {
        this.resultSet = resultSet;

        try {
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; ++i) {
                columnNames.add(resultSet.getMetaData().getColumnName(i + 1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            return new Column<T>(new ColumnDefinition<T>(columnName, new NullColumnType<T>()),
                    (T) resultSet.getObject(columnName));
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
        return columnNames.contains(columnDefinition.getName().toUpperCase());
    }

    @Override
    public void setColumn(Column<?> column) {
        throw new MethodNotFoundException("The method is not implemented");
    }

    private class NullColumnType<T extends Comparable<T>> extends ConvertableColumnType<T> {

        @SuppressWarnings("unchecked")
        @Override
        public Class<T> getCoreType() {
            return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        @Override
        protected boolean isValueCompatible(String value) {
            return false;
        }
    }
}

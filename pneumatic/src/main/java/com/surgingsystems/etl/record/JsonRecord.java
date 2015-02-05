package com.surgingsystems.etl.record;

import java.util.Iterator;
import java.util.List;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class JsonRecord implements Record {
    
    public JsonRecord() { }
    
    // Should this be a JsonSchema?
    public JsonRecord(Schema schema) {
        
    }

    @Override
    public <T extends Comparable<T>> T getValueFor(ColumnDefinition<T> columnDefinition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Comparable<T>> Column<T> getColumnFor(ColumnDefinition<T> columnDefinition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Comparable<T>> Column<T> getColumnForName(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column<?>> getColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasColumnFor(ColumnDefinition<?> columnDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T extends Comparable<T>> T getValueForName(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Column<?>> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setColumn(Column<?> column) {
        // TODO Auto-generated method stub
        
    }

}

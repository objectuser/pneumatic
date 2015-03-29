package com.surgingsystems.etl.schema;

import java.util.Collection;
import java.util.Iterator;

import com.surgingsystems.etl.record.Record;

public class JsonSchema implements Schema {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains(ColumnDefinition<?> columnDefinition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ColumnDefinition<? extends Comparable<?>> getColumnForName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<ColumnDefinition<? extends Comparable<?>>> getColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<ColumnDefinition<? extends Comparable<?>>> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Record createEmptyRecord() {
        // TODO Auto-generated method stub
        return null;
    }

}

package com.surgingsystems.etl.record;

import java.util.Comparator;

import com.surgingsystems.etl.schema.ColumnDefinition;

public class SingleColumnComparator<T extends Comparable<T>> implements Comparator<Record> {

    private ColumnDefinition<T> columnDefinition;

    public SingleColumnComparator() {
    }

    public SingleColumnComparator(ColumnDefinition<T> columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public void setColumnDefinition(ColumnDefinition<T> columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    @Override
    public int compare(Record o1, Record o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null && o2 != null) {
            return -1;
        } else if (o1 != null && o2 == null) {
            return 1;
        } else {
            T v1 = o1.getValueFor(columnDefinition);
            T v2 = o2.getValueFor(columnDefinition);
            return v1.compareTo(v2);
        }
    }

}

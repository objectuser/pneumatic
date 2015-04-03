package com.surgingsystems.etl.yamldsl;

import com.surgingsystems.etl.schema.ColumnDefinition;

class ColumnMappingAdapter {

    private ColumnDefinition<?> from;

    private ColumnDefinition<?> to;

    public ColumnDefinition<?> getFrom() {
        return from;
    }

    public void setFrom(ColumnDefinition<?> from) {
        this.from = from;
    }

    public ColumnDefinition<?> getTo() {
        return to;
    }

    public void setTo(ColumnDefinition<?> to) {
        this.to = to;
    }

}

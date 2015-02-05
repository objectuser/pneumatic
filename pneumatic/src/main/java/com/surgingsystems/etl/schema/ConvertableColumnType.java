package com.surgingsystems.etl.schema;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public abstract class ConvertableColumnType<T extends Comparable<T>> implements ColumnType<T> {

    @Autowired
    private ConversionService conversionService;

    @Override
    public boolean isCompatible(Object value) {
        return value == null
                || (conversionService.canConvert(value.getClass(), getCoreType()) && (!(value instanceof String)
                        || StringUtils.isEmpty((String) value) || isValueCompatible((String) value)));
    }

    protected abstract boolean isValueCompatible(String value);

    @Override
    public T convert(Object value) {
        if ((value instanceof String) && StringUtils.isEmpty((String) value)) {
            return null;
        } else {
            return conversionService.convert(value, getCoreType());
        }
    }

    @Override
    public <U extends Comparable<U>> boolean isColumnCompatible(Column<U> column) {
        Object value = column.getValue();
        return isCompatible(value);
    }

    @Override
    public int hashCode() {
        return getCoreType().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || hashCode() != obj.hashCode() || !getClass().equals(obj.getClass())) {
            return false;
        } else {
            return true;
        }
    }
}

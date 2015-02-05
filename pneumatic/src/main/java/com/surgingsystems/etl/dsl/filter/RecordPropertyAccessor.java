package com.surgingsystems.etl.dsl.filter;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;

/**
 * Enables the use of records in SpEL expressions as properties or map indexes.
 * 
 * <pre>
 * inputRecord.ColumName
 * inputRecord["ColumnName"]
 * </pre>
 */
public class RecordPropertyAccessor implements PropertyAccessor {

    public RecordPropertyAccessor() {
    }

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class<?>[] { Record.class };
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return true;
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        Record record = (Record) target;
        Column<?> column = record.getColumnForName(name);
        return new TypedValue(column.getValue());
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        Record record = (Record) target;
        Column column = record.getColumnForName(name);
        column.setValue((Comparable) newValue);
    }

}

package com.surgingsystems.etl.filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class DataMapping {

    private Map<String, String> columnNameToColumnNameMapping = new HashMap<String, String>();

    public DataRecord mapRecordFromResultSet(ResultSet resultSet, Schema schema) throws SQLException {
	DataRecord result = new DataRecord();
	for (Map.Entry<String, String> entry : columnNameToColumnNameMapping.entrySet()) {
	    Object value = resultSet.getObject(entry.getKey());
	    ColumnDefinition<? extends Comparable<?>> columnDefinition = schema.getColumnForName(entry.getValue());
	    result.addColumn(columnDefinition.applyToValue(value));
	}
	return result;
    }
    
    public void addMapping(String inputName, String outputName) {
        columnNameToColumnNameMapping.put(inputName, outputName);
    }
}

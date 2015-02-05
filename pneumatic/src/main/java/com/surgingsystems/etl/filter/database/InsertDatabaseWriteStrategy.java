package com.surgingsystems.etl.filter.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class InsertDatabaseWriteStrategy implements DatabaseWriteStrategy {

    private String insertStatement;
    
    private JdbcTemplate jdbcTemplate;
    
    private String tableName;

    @Override
    public void initialize(DataSource dataSource, Schema schema) {

        StringBuilder insertBuilder = new StringBuilder("insert into ").append(tableName).append(" (");
        StringBuilder valuesBuilder = new StringBuilder(") values (");

        for (Iterator<ColumnDefinition<? extends Comparable<?>>> columnDefinitionIterator = schema
                .getColumnDefinitions().iterator(); columnDefinitionIterator.hasNext();) {

            ColumnDefinition<? extends Comparable<?>> columnDefinition = columnDefinitionIterator.next();

            insertBuilder.append(columnDefinition.getName());
            if (columnDefinitionIterator.hasNext()) {
                insertBuilder.append(", ");
            }

            valuesBuilder.append("?");
            if (columnDefinitionIterator.hasNext()) {
                valuesBuilder.append(", ");
            }
        }

        valuesBuilder.append(")");
        
        insertBuilder.append(valuesBuilder);

        insertStatement = insertBuilder.toString();
        
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(Record record) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement =  connection.prepareStatement(insertStatement);
                
                int i = 0;
                for (Column<?> column : record) {
                    statement.setObject(++i, column.getValue());
                }
                
                return statement;
            }
        });
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

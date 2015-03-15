package com.surgingsystems.etl.filter.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class InsertDatabaseWriteStrategy implements DatabaseWriteStrategy {

    private static Logger logger = LogManager.getFormatterLogger(InsertDatabaseWriteStrategy.class);

    private String insertStatement;

    private Connection connection;

    private PreparedStatement ps;

    private String tableName;

    private int batchSize = 1000;

    private List<Record> batch = new ArrayList<>(batchSize);

    private int batchCount = 0;

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

        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(insertStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(Record record) {
        batch.add(record);
        ++batchCount;

        if (batchCount >= batchSize) {
            writeBatch();
            batch.clear();
            batchCount = 0;
        }
    }

    @Override
    public void close() {
        try {
            writeBatch();
            batch.clear();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBatch() {
        logger.info("Writing batch of size %d", batch.size());

        try {
            for (Record record : batch) {
                int j = 1;
                for (Column<?> column : record) {
                    ps.setObject(j++, column.getValue());
                }
                ps.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

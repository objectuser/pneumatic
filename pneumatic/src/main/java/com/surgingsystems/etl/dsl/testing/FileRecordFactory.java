package com.surgingsystems.etl.dsl.testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import com.surgingsystems.etl.filter.FlatFileRecordReader;
import com.surgingsystems.etl.record.ArrayRecordParser;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordParser;
import com.surgingsystems.etl.schema.Schema;

public class FileRecordFactory implements RecordFactory {

    private FlatFileRecordReader flatFileRecordReader;

    private Schema schema;

    private RecordParser<String[]> recordParser = new ArrayRecordParser();

    private List<Record> records = new ArrayList<Record>();

    public void readRecords() {
        try {
            flatFileRecordReader.open(new ExecutionContext());

            String[] values = null;
            while ((values = flatFileRecordReader.read()) != null) {
                records.add(recordParser.parse(values, schema));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to read records from file", e);
        }
    }

    @PostConstruct
    public void setupRejectionStrategy() {
        DefaultLineMapper<String[]> lineMapper = new DefaultLineMapper<String[]>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new ArrayFieldSetMapper());
        flatFileRecordReader.setLineMapper(lineMapper);
    }

    @Override
    public Iterator<Record> iterator() {
        return new Iterator<Record>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return records.size() > index;
            }

            @Override
            public Record next() {
                if (hasNext()) {
                    return records.get(index++);
                } else {
                    return null;
                }
            }

        };
    }

    public FlatFileRecordReader getFlatFileRecordReader() {
        return flatFileRecordReader;
    }

    public void setFlatFileRecordReader(FlatFileRecordReader flatFileRecordReader) {
        this.flatFileRecordReader = flatFileRecordReader;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public void setSchema(Schema schema) {
        this.schema = schema;

        readRecords();
    }
}

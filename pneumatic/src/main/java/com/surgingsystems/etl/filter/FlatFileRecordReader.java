package com.surgingsystems.etl.filter;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.core.io.Resource;

public class FlatFileRecordReader implements ItemReader<String[]> {
    
    private FlatFileItemReader<String[]> delegate = new FlatFileItemReader<String[]>();
    
    private Resource resource;
    
    public FlatFileRecordReader() {
    }
    
    public FlatFileRecordReader(Resource resource) {
        
        setResource(resource);
        
        DefaultLineMapper<String[]> lineMapper = new DefaultLineMapper<String[]>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new ArrayFieldSetMapper());
        setLineMapper(lineMapper);
    }

    @Override
    public String[] read() throws Exception {
        return delegate.read();
    }

    public void setLinesToSkip(int linesToSkip) {
        delegate.setLinesToSkip(linesToSkip);
    }

    public void setLineMapper(LineMapper<String[]> lineMapper) {
        delegate.setLineMapper(lineMapper);
    }

    public void close() throws ItemStreamException {
        delegate.close();
    }

    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    public void setResource(Resource resource) {
        this.resource = resource;
        delegate.setResource(resource);
    }
    
    public Resource getResource() {
        return resource;
    }
    
    public void setLineRange(Range range) {
        delegate.setLinesToSkip(range.getMin());
        delegate.setMaxItemCount(range.getMax() - range.getMin() + 1);
    }
    
    
}

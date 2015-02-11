package com.surgingsystems.etl.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.surgingsystems.etl.context.EtlContextProvider;
import com.surgingsystems.etl.filter.expression.EtlExpressionHelper;

public class FlatFileRecordReader implements ItemReader<String[]> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EtlContextProvider etlContextProvider;

    private FlatFileItemReader<String[]> delegate = new FlatFileItemReader<String[]>();

    private Resource resource;

    private String resourceExpression;

    private EtlExpressionHelper expressionHelper = new EtlExpressionHelper();

    public FlatFileRecordReader() {
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
        if (resource != null) {
            delegate.setResource(resource);
        } else if (!StringUtils.isEmpty(resourceExpression)) {
            expressionHelper.applyContext(etlContextProvider);
            String resourceValue = (String) expressionHelper.evaluate(resourceExpression);
            Resource resource = applicationContext.getResource(resourceValue);
            delegate.setResource(resource);
        } else {
            throw new IllegalArgumentException("A resource location or expression must be provided");
        }
        delegate.open(executionContext);
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public String getResourceExpression() {
        return resourceExpression;
    }

    public void setResourceExpression(String resourceExpression) {
        this.resourceExpression = resourceExpression;
    }

    public void setLineRange(Range range) {
        delegate.setLinesToSkip(range.getMin());
        delegate.setMaxItemCount(range.getMax() - range.getMin() + 1);
    }

}

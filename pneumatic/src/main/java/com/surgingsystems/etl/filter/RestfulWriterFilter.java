package com.surgingsystems.etl.filter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.schema.AcceptingRecordValidator;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.SchemaRecordValidator;
import com.surgingsystems.etl.schema.StringColumnType;

/**
 * Write records to a RESTful service interface.
 */
public class RestfulWriterFilter extends SingleInputFilter {

    private Logger logger = LogManager.getFormatterLogger(RestfulWriterFilter.class);

    @Autowired
    private StringColumnType stringColumnType;

    @Autowired
    private RestOperations restOperations;

    private HttpMethod httpMethod;

    private String url;

    private Schema inputSchema;

    private String[] requestParameters;

    private RecordValidator inputRecordValidator;

    private RejectRecordStrategy rejectRecordStrategy;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void validate() {

        Assert.notNull(url, "The request URL is required");
        Assert.notNull(getInput(), "The input is required");
        Assert.notNull(inputSchema, "The input schema is required");

        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            requestParameters = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); ++i) {
                String group = matcher.group(i + 1);
                requestParameters[i] = group;
                Assert.notNull(inputSchema.getColumnForName(group), "No column definition for request parameter: "
                        + group);
            }
        }

        if (inputSchema == null) {
            inputRecordValidator = new AcceptingRecordValidator();
        } else {
            inputRecordValidator = new SchemaRecordValidator(inputSchema);

            for (String requestParameter : requestParameters) {
                ColumnDefinition<?> columnDefinition = inputSchema.getColumnForName(requestParameter);
                Assert.notNull(columnDefinition, "The input schema must have a column defined for " + requestParameter);
            }
        }
    }

    @Override
    protected void process(Record inputRecord) throws Exception {

        if (!inputRecordValidator.accepts(inputRecord)) {
            rejectRecordStrategy.rejected(inputRecord);

        } else {

            Map<String, String> map = inputRecord.toMap();
            String json = objectMapper.writeValueAsString(map);
            if (logger.isTraceEnabled()) {
                logger.trace("Making request with message: %s", json);
            }

            Object[] requestValues = new String[requestParameters.length];
            for (int i = 0; i < requestValues.length; ++i) {
                Column<?> column = inputRecord.getColumnForName(requestParameters[i]);
                requestValues[i] = stringColumnType.convert(column.getValue());
            }

            HttpEntity<String> requestEntity = new HttpEntity<String>(json);
            ResponseEntity<String> response = restOperations.exchange(url, httpMethod, requestEntity, String.class,
                    requestValues);
            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.warn("Request rejected with <%s>", response.getStatusCode());
                rejectRecordStrategy.rejected(inputRecord);
            }
        }
    }

    @Override
    protected void postProcess() throws Exception {
        rejectRecordStrategy.close();
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    public RejectRecordStrategy getRejectRecordStrategy() {
        return rejectRecordStrategy;
    }

    public void setRejectRecordStrategy(RejectRecordStrategy rejectRecordStrategy) {
        this.rejectRecordStrategy = rejectRecordStrategy;
    }

}

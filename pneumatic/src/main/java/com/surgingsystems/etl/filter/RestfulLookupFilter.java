package com.surgingsystems.etl.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surgingsystems.etl.filter.mapping.Mapping;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordValidator;
import com.surgingsystems.etl.schema.AcceptingRecordValidator;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.SchemaRecordValidator;
import com.surgingsystems.etl.schema.StringColumnType;

/**
 * This filter provides the following features:
 * <ul>
 * <li>A request URL by which lookups are performed against restful services.</li>
 * <li>A single input.</li>
 * <li>A single output.</li>
 * <li>A schema defining the input.</li>
 * <li>A schema defining the output.</li>
 * <li>A schema defining the result of the restful lookup.</li>
 * <li>Values on on the input record may be used to map to elements of the
 * request URL using the Spring {parameter} format: the parameter names must
 * have matching column names on the input schema. The output schema may mix
 * values on the input and restful lookup. The response schema defines this
 * mapping.</li>
 * </ul>
 */
public class RestfulLookupFilter extends SingleInputFilter {

    private Logger logger = LogManager.getFormatterLogger(RestfulLookupFilter.class);

    @Autowired
    private StringColumnType stringColumnType;

    @Autowired
    private RestOperations restOperations;

    private String requestUrl;

    private HttpMethod httpMethod;

    private Schema inputSchema;

    private Schema responseSchema;

    private Schema outputSchema;

    private Pipe output;

    private String[] requestParameters;

    private Mapping mapping = new Mapping();

    private RecordValidator inputRecordValidator;

    private RecordValidator outputRecordValidator;

    private RejectRecordStrategy rejectRecordStrategy;

    @PostConstruct
    public void validate() {

        Assert.notNull(requestUrl, "The request URL is required");
        Assert.notNull(getInput(), "The input is required");
        Assert.notNull(inputSchema, "The input schema is required");
        Assert.notNull(responseSchema, "The input schema is required");
        Assert.notNull(outputSchema, "The input schema is required");
        Assert.notNull(output, "The output is required");

        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(requestUrl);
        if (matcher.find()) {
            requestParameters = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); ++i) {
                String group = matcher.group(i + 1);
                requestParameters[i] = group;
                Assert.notNull(inputSchema.getColumnForName(group), "No column definition for request parameter: "
                        + group);
            }
        }

        Set<ColumnDefinition<?>> outputColumnDefinitions = new HashSet<ColumnDefinition<?>>();
        outputColumnDefinitions.addAll(outputSchema.getColumns());
        Set<ColumnDefinition<?>> inputColumnDefinitions = new HashSet<ColumnDefinition<?>>();
        inputColumnDefinitions.addAll(inputSchema.getColumns());
        inputColumnDefinitions.addAll(responseSchema.getColumns());
        Assert.isTrue(inputColumnDefinitions.containsAll(outputColumnDefinitions),
                "The input and response schemas must have columns matching the names of those in the output schema");

        mapping.setOutputSchema(outputSchema);

        if (inputSchema == null) {
            inputRecordValidator = new AcceptingRecordValidator();
        } else {
            inputRecordValidator = new SchemaRecordValidator(inputSchema);
        }

        outputRecordValidator = new SchemaRecordValidator(outputSchema);
    }

    @Override
    protected void process(Record inputRecord) throws Exception {

        if (!inputRecordValidator.accepts(inputRecord)) {
            rejectRecordStrategy.rejected(inputRecord);

        } else {

            Object[] requestValues = new String[requestParameters.length];
            for (int i = 0; i < requestValues.length; ++i) {
                Column<?> column = inputRecord.getColumnForName(requestParameters[i]);
                requestValues[i] = stringColumnType.convert(column.getValue());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restOperations.exchange(requestUrl, httpMethod, requestEntity,
                    String.class, requestValues);

            if (logger.isTraceEnabled()) {
                logger.trace("JSON response: " + response.getBody());
            }

            Record responseRecord = createResponseRecord(response.getBody());
            Record outputRecord = mapping.map(inputRecord, responseRecord);
            if (outputRecord != null && outputRecordValidator.accepts(outputRecord)) {
                output.put(outputRecord);
            } else {
                rejectRecordStrategy.rejected(inputRecord);
            }
        }
    }

    @Override
    protected void postProcess() throws Exception {
        output.closedForInput();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public Schema getResponseSchema() {
        return responseSchema;
    }

    public void setResponseSchema(Schema responseSchema) {
        this.responseSchema = responseSchema;
    }

    private Record createResponseRecord(String json) {

        try {
            Map<String, String> map = new HashMap<String, String>();
            ObjectMapper mapper = new ObjectMapper();

            map = mapper.readValue(json, new TypeReference<HashMap<String, String>>() {
            });

            return createRecord(map);

        } catch (Exception exception) {
            throw new RuntimeException("Unable to handle request", exception);
        }
    }

    private Record createRecord(Map<String, String> input) {
        DataRecord responseRecord = new DataRecord();
        for (ColumnDefinition<?> columnDefinition : responseSchema) {
            String stringValue = input.get(columnDefinition.getName());
            Column<?> column = columnDefinition.applyToValue(stringValue);
            responseRecord.addColumn(column);
        }
        return responseRecord;
    }
}

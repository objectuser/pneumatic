package com.surgingsystems.etl.filter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.JsonRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.schema.StringColumnType;
import com.surgingsystems.etl.test.filter.pipe.PipeUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestfulWriterFilterTest.Config.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RestfulWriterFilterTest {

    @Resource(name = "restfulWriter")
    private RestfulWriterFilter restfulWriterFilter;

    @Resource(name = "input")
    private Pipe input;

    @Resource(name = "rejectionOutput")
    private Pipe rejectionOutput;

    @Resource(name = "inputSchema")
    private Schema schema;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private GenericApplicationContext applicationContext;

    @Autowired
    private RestOperations restOperations;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        restfulWriterFilter.setUrl(null);
        restfulWriterFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        restfulWriterFilter.setInput(null);
        restfulWriterFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputSchemaIsRequired() {
        restfulWriterFilter.setInputSchema(null);
        restfulWriterFilter.validate();
    }

    @Test
    public void accept() throws Exception {

        JsonRecord bronson = new JsonRecord(schema, "Bronson", 5, 5000.0);
        JsonRecord mach6 = new JsonRecord(schema, "Mach6", 15, 6000.0);
        JsonRecord trance = new JsonRecord(schema, "Trance", 10, 2000.0);

        when(
                restOperations.exchange(any(String.class), eq(HttpMethod.PUT),
                        eq(new HttpEntity<String>(mapper.writeValueAsString(bronson.toMap()))), eq(String.class),
                        any(Object[].class))).thenReturn(new ResponseEntity<String>(HttpStatus.ACCEPTED));

        when(
                restOperations.exchange(any(String.class), eq(HttpMethod.PUT),
                        eq(new HttpEntity<String>(mapper.writeValueAsString(mach6.toMap()))), eq(String.class),
                        any(Object[].class))).thenReturn(new ResponseEntity<String>(HttpStatus.BAD_REQUEST));

        when(
                restOperations.exchange(any(String.class), eq(HttpMethod.PUT),
                        eq(new HttpEntity<String>(mapper.writeValueAsString(trance.toMap()))), eq(String.class),
                        any(Object[].class))).thenReturn(new ResponseEntity<String>(HttpStatus.ACCEPTED));

        input.put(bronson);
        input.put(mach6);
        input.put(trance);

        input.closedForInput();

        restfulWriterFilter.filter();

        List<Record> rejectedRecords = PipeUtility.toList(rejectionOutput);
        Assert.assertEquals("Rejection is right", Arrays.asList(mach6), rejectedRecords);
    }

    @ImportResource({ "com/surgingsystems/etl/filter/restful-writer-filter-test.xml" })
    public static class Config {

        @Bean
        public PropertyPlaceholderConfigurer propertyConfigurer() throws IOException {
            PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
            props.setLocations(new ClassPathResource("etl.properties"));
            return props;
        }

        @Bean
        public RestOperations restOperations() {
            return mock(RestOperations.class);
        }

        @Bean
        @DependsOn("conversionService")
        public StringColumnType stringColumnType() {
            return new StringColumnType();
        }

        @Bean
        @DependsOn("conversionService")
        public IntegerColumnType integerColumnType() {
            return new IntegerColumnType();
        }

        @Bean
        @DependsOn("conversionService")
        public DecimalColumnType decimalColumnType() {
            return new DecimalColumnType();
        }

        @Bean(name = "conversionService")
        public ConversionServiceFactoryBean conversionService() {
            return new ConversionServiceFactoryBean();
        }

    }
}

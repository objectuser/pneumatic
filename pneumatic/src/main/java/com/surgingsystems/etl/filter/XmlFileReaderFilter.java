package com.surgingsystems.etl.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.util.Assert;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.XmlRecord;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.XmlSchema;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XmlFileReaderFilter extends GuardedFilter {

    private static Logger logger = LogManager.getFormatterLogger(XmlFileReaderFilter.class);

    private Resource resource;

    private XmlSchema schema;

    private Pipe output;

    @PostConstruct
    public void validate() {
        logger.trace("XML Reader (%s): validating", getName());
        Assert.notNull(getName(), "The name is required");
        Assert.notNull(output, "The output pipe is required");
        Assert.notNull(schema, "The output schema is required");
        Assert.notNull(resource, "The file resource is required");
    }

    @SuppressWarnings("serial")
    @Override
    protected void filter() throws Exception {

        StaxEventItemReader<Map<String, String>> itemReader = new StaxEventItemReader<Map<String, String>>();
        itemReader.setFragmentRootElementName(schema.getFragmentRootElementName());
        itemReader.setResource(resource);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setConverters(new MapEntryConverter());
        marshaller.setAliases(new LinkedHashMap<String, Object>() {
            {
                put(schema.getFragmentRootElementName(), LinkedHashMap.class.getName());
            }
        });

        itemReader.setUnmarshaller(marshaller);

        itemReader.open(new ExecutionContext());

        try {
            Map<String, String> input = null;
            while ((input = itemReader.read()) != null) {

                Record record = createRecord(input);

                recordProcessed();
                logRecord(record);

                output.put(record);
            }

            output.closedForInput();

            logSummary();

        } finally {
            itemReader.close();
        }
    }

    private Record createRecord(Map<String, String> input) {
        XmlRecord xmlRecord = new XmlRecord();
        for (ColumnDefinition<?> columnDefinition : schema) {
            String stringValue = input.get(columnDefinition.getName());
            Column<?> column = columnDefinition.applyToValue(stringValue);
            xmlRecord.addColumn(column);
        }
        return xmlRecord;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public XmlSchema getSchema() {
        return schema;
    }

    public void setSchema(XmlSchema schema) {
        this.schema = schema;
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public static class MapEntryConverter implements Converter {

        @Override
        public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
            return Map.class.isAssignableFrom(clazz);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            Map<String, String> map = (Map<String, String>) value;
            for (Object obj : map.entrySet()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
                writer.startNode(entry.getKey());
                writer.setValue(entry.getValue());
                writer.endNode();
            }
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new LinkedHashMap<String, String>();

            while (reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName();
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }

    }

}

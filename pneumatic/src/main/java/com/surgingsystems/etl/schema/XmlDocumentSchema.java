package com.surgingsystems.etl.schema;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.core.io.Resource;

import com.surgingsystems.etl.record.Record;

/**
 * This should be used for document-oriented XML and not just records specified
 * in XML, which is what {@link XmlSchema} is for.
 * 
 * We should be able to use SpEL to traverse the XML as a {@link Map}.
 */
public class XmlDocumentSchema implements Schema {

    private String name;

    private Resource location;

    private String fragmentRootElementName;

    private Map<String, ColumnDefinition<? extends Comparable<?>>> columnNameToDefinitionMap = new LinkedHashMap<String, ColumnDefinition<? extends Comparable<?>>>();


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<ColumnDefinition<? extends Comparable<?>>> iterator() {
        return getColumnDefinitions().iterator();
    }

    /**
     * Validate the source against the XML schema definition. Note that this
     * requires the entire file to be read.
     */
    public boolean isValid(Resource xml) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Validator validator = factory.newSchema(new StreamSource(location.getInputStream())).newValidator();
            validator.validate(new StreamSource(xml.getInputStream()));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean contains(ColumnDefinition<?> columnDefinition) {
        return getColumnDefinitions().contains(columnDefinition);
    }

    @Override
    public ColumnDefinition<? extends Comparable<?>> getColumnForName(String name) {
        return columnNameToDefinitionMap.get(name);
    }

    @Override
    public Collection<ColumnDefinition<? extends Comparable<?>>> getColumnDefinitions() {
        return columnNameToDefinitionMap.values();
    }

    @Override
    public Record createEmptyRecord() {
        return null;
    }

    public void addColumnDefinition(ColumnDefinition<?> columnDefinition) {
        columnNameToDefinitionMap.put(columnDefinition.getName(), columnDefinition);
    }

    public void setColumnDefinitions(Collection<ColumnDefinition<? extends Comparable<?>>> columnDefinitions) {
        for (ColumnDefinition<? extends Comparable<?>> columnDefinition : columnDefinitions) {
            addColumnDefinition(columnDefinition);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFragmentRootElementName() {
        return fragmentRootElementName;
    }

    public void setFragmentRootElementName(String fragmentRootElementName) {
        this.fragmentRootElementName = fragmentRootElementName;
    }

    public Resource getLocation() {
        return location;
    }

    public void setLocation(Resource location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), getName(), columnNameToDefinitionMap.values());
    }
}

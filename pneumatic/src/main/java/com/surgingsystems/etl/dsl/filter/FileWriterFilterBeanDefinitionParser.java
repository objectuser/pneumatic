package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.FileWriterFilter;

public class FileWriterFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return FileWriterFilter.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element fileResourceElement = DomUtils.getChildElementByTagName(element, "fileResource");
        bean.addPropertyValue("fileResource", fileResourceElement.getAttribute("location"));
        Element outputElement = DomUtils.getChildElementByTagName(element, "input");
        bean.addPropertyReference("input", outputElement.getAttribute("ref"));
        Element schemaElement = DomUtils.getChildElementByTagName(element, "inputSchema");
        bean.addPropertyReference("schema", schemaElement.getAttribute("ref"));
    }
}

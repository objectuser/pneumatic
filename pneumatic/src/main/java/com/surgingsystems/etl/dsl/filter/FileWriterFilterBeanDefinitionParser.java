package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.FileWriterFilter;

public class FileWriterFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return FileWriterFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element fileResourceElement = DomUtils.getChildElementByTagName(element, "fileResource");
        bean.addPropertyValue("fileResource", fileResourceElement.getAttribute("location"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "inputSchema", "schema");
    }
}

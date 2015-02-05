package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.RestfulLookupFilter;

public class RestfulLookupBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return RestfulLookupFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element requestUrlElement = DomUtils.getChildElementByTagName(element, "requestUrl");
        bean.addPropertyValue("requestUrl", requestUrlElement.getAttribute("value"));

        Element inputElement = DomUtils.getChildElementByTagName(element, "input");
        bean.addPropertyReference("input", inputElement.getAttribute("ref"));

        Element inputSchemaElement = DomUtils.getChildElementByTagName(element, "inputSchema");
        bean.addPropertyReference("inputSchema", inputSchemaElement.getAttribute("ref"));

        Element outputElement = DomUtils.getChildElementByTagName(element, "output");
        bean.addPropertyReference("output", outputElement.getAttribute("ref"));

        Element outputSchemaElement = DomUtils.getChildElementByTagName(element, "outputSchema");
        bean.addPropertyReference("outputSchema", outputSchemaElement.getAttribute("ref"));

        Element responseSchemaElement = DomUtils.getChildElementByTagName(element, "responseSchema");
        bean.addPropertyReference("responseSchema", responseSchemaElement.getAttribute("ref"));

    }
}

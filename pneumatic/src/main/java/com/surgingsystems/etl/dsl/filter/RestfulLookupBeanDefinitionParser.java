package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.http.HttpMethod;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.RestfulLookupFilter;

public class RestfulLookupBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return RestfulLookupFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element httpElement = DomUtils.getChildElementByTagName(element, "http");
        bean.addPropertyValue("httpMethod", HttpMethod.valueOf(httpElement.getAttribute("method")));
        bean.addPropertyValue("requestUrl", httpElement.getAttribute("url"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "inputSchema", "inputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "outputSchema", "outputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "responseSchema", "responseSchema");
    }
}

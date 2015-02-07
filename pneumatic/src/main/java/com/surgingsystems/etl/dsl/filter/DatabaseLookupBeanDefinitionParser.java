package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.DatabaseLookupFilter;

public class DatabaseLookupBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return DatabaseLookupFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "inputSchema", "inputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "outputSchema", "outputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "dataSource", "dataSource");

        Element lookupElement = DomUtils.getChildElementByTagName(element, "lookup");
        
        Element sqlElement = DomUtils.getChildElementByTagName(lookupElement, "sql");
        String sql = sqlElement.getTextContent().trim();
        bean.addPropertyValue("sql", sql);

        ManagedList<String> parameters = new ManagedList<String>();
        List<Element> parameterElements = DomUtils.getChildElementsByTagName(lookupElement, "parameter");
        for (Element parameterElement : parameterElements) {
            parameters.add(parameterElement.getAttribute("value"));
        }
        
        bean.addPropertyValue("parameters", parameters);
    }
}

package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.DatabaseReaderFilter;

public class DatabaseReaderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return DatabaseReaderFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "outputSchema", "outputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "dataSource", "dataSource");

        Element sqlSelectElement = DomUtils.getChildElementByTagName(element, "sqlSelect");
        String sqlSelect = sqlSelectElement.getTextContent().trim();
        bean.addPropertyValue("sqlSelect", sqlSelect);

        ManagedList<Object> parameters = new ManagedList<Object>();
        List<Element> parameterElements = DomUtils.getChildElementsByTagName(element, "parameter");
        for (Element parameterElement : parameterElements) {
            parameters.add(parameterElement.getAttribute("value"));
        }
        
        bean.addPropertyValue("parameters", parameters);
    }
}

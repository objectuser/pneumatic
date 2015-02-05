package com.surgingsystems.etl.dsl.filter.database;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.database.ConfigurableDatabaseWriteStrategy;

public class ConfigurableDatabaseWriteStrategyBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ConfigurableDatabaseWriteStrategy.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        Element inputElement = DomUtils.getChildElementByTagName(element, "sql");
        String sql = inputElement.getTextContent().trim();
        bean.addPropertyValue("sql", sql);

        ManagedList<String> parameters = new ManagedList<String>();
        List<Element> parameterElements = DomUtils.getChildElementsByTagName(element, "parameter");
        for (Element parameterElement : parameterElements) {
            parameters.add(parameterElement.getAttribute("value"));
        }
        
        bean.addPropertyValue("parameters", parameters);
    }

}

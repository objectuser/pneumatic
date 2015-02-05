package com.surgingsystems.etl.dsl.testing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;

public class ColumnValueBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(ColumnValueBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Column.class;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        String name = element.getAttribute("column");
        ColumnDefinition<?> columnDefinition = new ColumnDefinition(name, null);
        bean.addPropertyValue("columnDefinition", columnDefinition);
        String value = element.getAttribute("value");
        bean.addPropertyValue("value", value);

        logger.trace("Building column with name/value (%s/%s)", name, value);
    }
}

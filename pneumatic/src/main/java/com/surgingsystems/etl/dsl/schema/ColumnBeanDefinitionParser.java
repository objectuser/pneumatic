package com.surgingsystems.etl.dsl.schema;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.surgingsystems.etl.schema.ColumnDefinition;

public class ColumnBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(ColumnBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ColumnDefinition.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String name = element.getAttribute("name");
        bean.addPropertyValue("name", name);
        String type = element.getAttribute("type");
        String nullable = element.getAttribute("nullable");
        if (!StringUtils.isEmpty(nullable)) {
            bean.addPropertyValue("nullable", nullable);
        }

        logger.trace("Building column (%s, %s)", name, type);

        if ("string".equals(type)) {
            bean.addPropertyReference("type", "stringColumnType");
        } else if ("integer".equals(type)) {
            bean.addPropertyReference("type", "integerColumnType");
        } else if ("decimal".equals(type)) {
            bean.addPropertyReference("type", "decimalColumnType");
        } else {
            logger.error("Unknown column type (%s)", type);
            throw new RuntimeException(String.format("Unknown column type (%s)", type));
        }
    }
}

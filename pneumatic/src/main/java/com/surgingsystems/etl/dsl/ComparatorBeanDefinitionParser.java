package com.surgingsystems.etl.dsl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.SpringBeanDefinitionParser;

public class ComparatorBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(ComparatorBeanDefinitionParser.class);

    public BeanDefinition parse(Element element, ParserContext nestedParserContext) {

        logger.trace("Creating comparator");

        Element columnElement = DomUtils.getChildElementByTagName(element, "column");
        Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
        if (columnElement != null) {
            SingleColumnComparatorBeanDefinitionParser parser = new SingleColumnComparatorBeanDefinitionParser();
            return parser.parse(element, nestedParserContext);
        } else if (beanElement != null) {
            SpringBeanDefinitionParser springBeanDefinitionParser = new SpringBeanDefinitionParser();
            return springBeanDefinitionParser.parse(beanElement, nestedParserContext);
        } else {
            return null;
        }
    }
}

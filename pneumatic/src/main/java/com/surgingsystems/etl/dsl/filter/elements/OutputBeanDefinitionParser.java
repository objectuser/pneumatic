package com.surgingsystems.etl.dsl.filter.elements;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.transformer.OutputCondition;

public class OutputBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(OutputBeanDefinitionParser.class);

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return OutputCondition.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        logger.trace("Building output config (%s)", element.getAttribute("name"));

        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");

        Element outputConditionElement = DomUtils.getChildElementByTagName(element, "outputCondition");
        if (outputConditionElement != null) {
            String value = outputConditionElement.getAttribute("value");
            if (StringUtils.isEmpty(value)) {
                value = outputConditionElement.getTextContent();
            }

            bean.addPropertyValue("conditionExpression", value);
        }
    }
}

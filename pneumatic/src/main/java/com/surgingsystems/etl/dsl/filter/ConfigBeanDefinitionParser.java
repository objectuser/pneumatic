package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.transformer.TransformerFilterOutputConfiguration;

public class ConfigBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(ConfigBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return TransformerFilterOutputConfiguration.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        logger.trace("Building output config (%s)", element.getAttribute("outputName"));

        bean.addPropertyValue("name", element.getAttribute("outputName"));
        bean.addPropertyValue("recordName", element.getAttribute("recordName"));

        Element pipeElement = DomUtils.getChildElementByTagName(element, "output");
        bean.addPropertyReference("output", pipeElement.getAttribute("ref"));
        Element schemaElement = DomUtils.getChildElementByTagName(element, "schema");
        bean.addPropertyReference("schema", schemaElement.getAttribute("ref"));
        
        parseExpressions(element, bean);
        
        Element outputConditionElement = DomUtils.getChildElementByTagName(element, "outputCondition");
        if (outputConditionElement != null) {
            bean.addPropertyValue("outputCondition", outputConditionElement.getTextContent());
        }
    }

    private void parseExpressions(Element element, BeanDefinitionBuilder bean) {
        List<Element> expressionsElements = DomUtils.getChildElementsByTagName(element, "expression");
        
        ManagedList<String> expressions = new ManagedList<String>();
        for (Element expressionElement : expressionsElements) {
            String value = expressionElement.getTextContent();
            expressions.add(value);
        }

        bean.addPropertyValue("expressions", expressions);
    }
}

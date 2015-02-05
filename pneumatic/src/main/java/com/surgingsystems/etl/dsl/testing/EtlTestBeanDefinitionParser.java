package com.surgingsystems.etl.dsl.testing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.testing.EtlTest;

public class EtlTestBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(EtlTestBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return EtlTest.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        String name = element.getAttribute("name");

        logger.trace("Building test (%s)", name);

        bean.addPropertyValue("name", name);

        Element schemaElement = DomUtils.getChildElementByTagName(element, "filter");
        bean.addPropertyReference("filter", schemaElement.getAttribute("ref"));

        {
            Element whenElement = DomUtils.getChildElementByTagName(element, "when");

            Element inputElement = DomUtils.getChildElementByTagName(whenElement, "input");
            if (inputElement != null) {
                bean.addPropertyReference("input", inputElement.getAttribute("ref"));
            }

            Element dataSetElement = DomUtils.getChildElementByTagName(whenElement, "dataSet");
            if (dataSetElement != null) {
                bean.addPropertyReference("inputDataSet", dataSetElement.getAttribute("ref"));
            }
        }

        {
            Element whenElement = DomUtils.getChildElementByTagName(element, "expect");
            
            Element outputElement = DomUtils.getChildElementByTagName(whenElement, "output");
            if (outputElement != null) {
                bean.addPropertyReference("output", outputElement.getAttribute("ref"));
            }

            Element dataSetElement = DomUtils.getChildElementByTagName(whenElement, "dataSet");
            if (dataSetElement != null) {
                bean.addPropertyReference("outputDataSet", dataSetElement.getAttribute("ref"));
            }
        }
    }
}

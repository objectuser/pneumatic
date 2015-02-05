package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.ComparatorBeanDefinitionParser;
import com.surgingsystems.etl.filter.JoinFilter;

public class JoinBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return JoinFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element leftInputElement = DomUtils.getChildElementByTagName(element, "leftInput");
        bean.addPropertyReference("leftInput", leftInputElement.getAttribute("ref"));

        Element rightInputElement = DomUtils.getChildElementByTagName(element, "rightInput");
        bean.addPropertyReference("rightInput", rightInputElement.getAttribute("ref"));

        Element outputElement = DomUtils.getChildElementByTagName(element, "output");
        bean.addPropertyReference("output", outputElement.getAttribute("ref"));

        Element outputSchemaElement = DomUtils.getChildElementByTagName(element, "outputSchema");
        bean.addPropertyReference("outputSchema", outputSchemaElement.getAttribute("ref"));
        
        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        Element comparatorElement = DomUtils.getChildElementByTagName(element, "comparator");
        ComparatorBeanDefinitionParser comparatorBeanDefinitionParser = new ComparatorBeanDefinitionParser();
        BeanDefinition comparator = comparatorBeanDefinitionParser.parse(comparatorElement, nestedParserContext);

        bean.addPropertyValue("comparator", comparator);
    }
}

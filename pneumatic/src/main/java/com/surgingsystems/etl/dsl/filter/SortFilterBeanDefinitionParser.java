package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.ComparatorBeanDefinitionParser;
import com.surgingsystems.etl.filter.SortFilter;

public class SortFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return SortFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element inputElement = DomUtils.getChildElementByTagName(element, "input");
        bean.addPropertyReference("input", inputElement.getAttribute("ref"));

        Element outputElement = DomUtils.getChildElementByTagName(element, "output");
        bean.addPropertyReference("output", outputElement.getAttribute("ref"));

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        Element comparatorElement = DomUtils.getChildElementByTagName(element, "comparator");
        ComparatorBeanDefinitionParser comparatorBeanDefinitionParser = new ComparatorBeanDefinitionParser();
        BeanDefinition comparator = comparatorBeanDefinitionParser.parse(comparatorElement, nestedParserContext);

        bean.addPropertyValue("comparator", comparator);
    }
}

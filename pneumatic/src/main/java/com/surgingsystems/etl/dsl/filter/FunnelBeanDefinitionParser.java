package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.FunnelFilter;

public class FunnelBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return FunnelFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        ManagedList<Object> inputs = new ManagedList<Object>();
        List<Element> inputElements = DomUtils.getChildElementsByTagName(element, "input");
        for (Element inputElement : inputElements) {
            inputs.add(new RuntimeBeanReference(inputElement.getAttribute("ref")));
        }
        
        bean.addPropertyValue("inputs", inputs);

        Element outputElement = DomUtils.getChildElementByTagName(element, "output");
        bean.addPropertyReference("output", outputElement.getAttribute("ref"));
    }
}

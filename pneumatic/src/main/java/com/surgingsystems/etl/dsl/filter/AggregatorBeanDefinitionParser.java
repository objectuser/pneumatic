package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.function.FunctionParser;
import com.surgingsystems.etl.dsl.schema.RejectionBeanDefinitionParser;
import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.AggregatorFilter;

public class AggregatorBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return AggregatorFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "inputSchema", "inputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "outputSchema", "outputSchema");

        Element functionElement = DomUtils.getChildElementByTagName(element, "function");

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        List<Element> children = DomUtils.getChildElements(functionElement);
        Element functionDefinitionElement = children.get(0);
        FunctionParser functionParser = new FunctionParser();
        BeanDefinition function = functionParser.parse(functionDefinitionElement, nestedParserContext);
        bean.addPropertyValue("function", function);

        Element rejectionElement = DomUtils.getChildElementByTagName(element, "rejection");
        if (rejectionElement != null) {
            RejectionBeanDefinitionParser rejectionBeanDefinitionParser = new RejectionBeanDefinitionParser();
            BeanDefinition rejectionBeanDefinition = rejectionBeanDefinitionParser.parse(rejectionElement,
                    nestedParserContext);
            bean.addPropertyValue("rejectRecordStrategy", rejectionBeanDefinition);
        }
    }
}

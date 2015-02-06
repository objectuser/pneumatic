package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.CopyFilter;

public class CopyBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return CopyFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        parseOutputs(element, bean, nestedParserContext);
    }

    private void parseOutputs(Element element, BeanDefinitionBuilder bean,
            ParserContext nestedParserContext) {
        List<Element> outputElements = DomUtils.getChildElementsByTagName(element, "output");
        ManagedList<Object> outputs = new ManagedList<Object>();
        for (Element outputElement : outputElements) {
            RuntimeBeanReference reference = new RuntimeBeanReference(outputElement.getAttribute("ref"));
            outputs.add(reference);
        }
        
        bean.addPropertyValue("outputs", outputs);
    }
}

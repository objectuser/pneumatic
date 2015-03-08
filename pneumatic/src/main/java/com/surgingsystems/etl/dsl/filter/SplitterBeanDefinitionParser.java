package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.filter.elements.OutputBeanDefinitionParser;
import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.SplitterFilter;

public class SplitterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return SplitterFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        parseOutputConfigurations(element, bean, nestedParserContext);
    }

    private void parseOutputConfigurations(Element element, BeanDefinitionBuilder bean,
            ParserContext nestedParserContext) {
        List<Element> configElements = DomUtils.getChildElementsByTagName(element, "outputConfiguration");
        ManagedList<Object> outputs = new ManagedList<Object>();
        for (Element configElement : configElements) {
            OutputBeanDefinitionParser parser = new OutputBeanDefinitionParser();
            outputs.add(parser.parse(configElement, nestedParserContext));
        }

        bean.addPropertyValue("outputConditions", outputs);
    }
}

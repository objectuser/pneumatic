package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.filter.elements.ConfigBeanDefinitionParser;
import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.TransformerFilter;

public class TransformerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return TransformerFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");

        parseVariables(element, bean);

        parseExpressions(element, bean);

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        parseOutputConfigurations(element, bean, nestedParserContext);
    }

    private void parseOutputConfigurations(Element element, BeanDefinitionBuilder bean,
            ParserContext nestedParserContext) {
        List<Element> configElements = DomUtils.getChildElementsByTagName(element, "outputConfiguration");
        ManagedList<Object> configs = new ManagedList<Object>();
        for (Element configElement : configElements) {
            ConfigBeanDefinitionParser parser = new ConfigBeanDefinitionParser();
            configs.add(parser.parse(configElement, nestedParserContext));
        }

        bean.addPropertyValue("outputConfigurations", configs);
    }

    private void parseVariables(Element element, BeanDefinitionBuilder bean) {
        List<Element> variableElements = DomUtils.getChildElementsByTagName(element, "variable");

        ManagedMap<String, String> variables = new ManagedMap<String, String>();
        for (Element variableElement : variableElements) {
            String name = variableElement.getAttribute("name");
            String value = variableElement.getTextContent();
            variables.put(name, value);
        }

        bean.addPropertyValue("variables", variables);
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

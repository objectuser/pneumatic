package com.surgingsystems.etl.dsl.function;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.function.CounterFunction;

public class CountFunctionBeanDefinitionParser extends FunctionColumnBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return CounterFunction.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        parseColumn(element, parserContext, bean, "out", "outputColumnDefinition");
    }
}

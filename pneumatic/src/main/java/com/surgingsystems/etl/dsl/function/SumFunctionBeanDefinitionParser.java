package com.surgingsystems.etl.dsl.function;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.function.SumFunction;

public class SumFunctionBeanDefinitionParser extends FunctionColumnBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return SumFunction.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        parseColumn(element, parserContext, bean, "in", "inputColumnDefinition");
        BeanDefinition outputColumnDefinition = parseColumn(element, parserContext, bean, "out", "outputColumnDefinition");
        
        BeanReference type = (BeanReference) outputColumnDefinition.getPropertyValues().get("columnType");
        if ("integerColumnType".equals(type.getBeanName())) {
            bean.addPropertyReference("sumStrategy", "sumIntegerStrategy");
        } else {
            bean.addPropertyReference("sumStrategy", "sumDecimalStrategy");
        }
    }
}

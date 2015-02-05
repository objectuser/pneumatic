package com.surgingsystems.etl.dsl.function;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.schema.ColumnBeanDefinitionParser;

public abstract class FunctionColumnBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected BeanDefinition parseColumn(Element element, ParserContext parserContext, BeanDefinitionBuilder bean, String io,
            String beanProperty) {

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        Element outElement = DomUtils.getChildElementByTagName(element, io);
        Element column = DomUtils.getChildElementByTagName(outElement, "column");
        ColumnBeanDefinitionParser parser = new ColumnBeanDefinitionParser();
        BeanDefinition columnDefinition = parser.parse(column, nestedParserContext);
        bean.addPropertyValue(beanProperty, columnDefinition);
        return columnDefinition;
    }
}

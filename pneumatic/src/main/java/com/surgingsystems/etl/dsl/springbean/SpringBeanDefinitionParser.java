package com.surgingsystems.etl.dsl.springbean;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Parse a Spring Bean.
 */
public class SpringBeanDefinitionParser extends AbstractBeanDefinitionParser {

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        BeanDefinitionParserDelegate pd = new BeanDefinitionParserDelegate(parserContext.getReaderContext());
        BeanDefinitionHolder bh = pd.parseBeanDefinitionElement(element, builder.getBeanDefinition());
        return (AbstractBeanDefinition) bh.getBeanDefinition();
    }

}

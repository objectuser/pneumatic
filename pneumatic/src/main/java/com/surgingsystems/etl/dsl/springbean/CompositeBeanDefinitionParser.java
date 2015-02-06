package com.surgingsystems.etl.dsl.springbean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parse a Spring Bean.
 */
public class CompositeBeanDefinitionParser {

    public void parse(Element parentElement, ParserContext parserContext, BeanDefinitionBuilder bean, String elementName, String property) {
        
        Element element = DomUtils.getChildElementByTagName(parentElement, elementName);

        String ref = element.getAttribute("ref");
        if (!StringUtils.isEmpty(ref)) {
            bean.addPropertyReference(property, ref);
        } else {
            Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
            if (beanElement != null) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                BeanDefinitionParserDelegate pd = new BeanDefinitionParserDelegate(parserContext.getReaderContext());
                BeanDefinitionHolder bh = pd.parseBeanDefinitionElement(beanElement, builder.getBeanDefinition());
                BeanDefinition bd = bh.getBeanDefinition();
                bean.addPropertyValue(property, bd);
            }
        }

    }

}

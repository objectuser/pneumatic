package com.surgingsystems.etl.dsl.springbean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parse a Spring Bean.
 */
public class CompositeBeanDefinitionParser {

    public void parse(Element parentElement, ParserContext parserContext, BeanDefinitionBuilder bean,
            String elementName, String property) {

        Element element = DomUtils.getChildElementByTagName(parentElement, elementName);

        String ref = element.getAttribute("ref");
        if (!StringUtils.isEmpty(ref)) {
            bean.addPropertyReference(property, ref);
        } else {
            NodeList children = element.getChildNodes();
            for (int i = 0; i < children.getLength(); ++i) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) child;
                    if ("bean".equals(beanElement.getLocalName())) {
                        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                        BeanDefinitionParserDelegate pd = new BeanDefinitionParserDelegate(
                                parserContext.getReaderContext());
                        BeanDefinitionHolder bh = pd.parseBeanDefinitionElement(beanElement,
                                builder.getBeanDefinition());
                        BeanDefinition bd = bh.getBeanDefinition();
                        bean.addPropertyValue(property, bd);
                    } else {
                        BeanDefinitionParserDelegate pd = new BeanDefinitionParserDelegate(
                                parserContext.getReaderContext());
                        BeanDefinition bd = pd.parseCustomElement(beanElement, bean.getBeanDefinition());
                        bean.addPropertyValue(property, bd);
                    }
                }
            }
        }

    }

}

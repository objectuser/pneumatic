package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.FileReaderFilter;

public class FileReaderFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return FileReaderFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        {
            ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                    parserContext.getDelegate(), bean.getBeanDefinition());

            Element fileResourceElement = DomUtils.getChildElementByTagName(element, "fileResource");

            FileBeanDefinitionParser fileBeanDefinitionParser = new FileBeanDefinitionParser();
            BeanDefinition fileBean = fileBeanDefinitionParser.parse(fileResourceElement, nestedParserContext);
            bean.addPropertyValue("itemReader", fileBean);
        }

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "outputSchema", "schema");
        Element skipElement = DomUtils.getChildElementByTagName(element, "skipLines");
        if (skipElement != null) {
            bean.addPropertyValue("skipLineCount", skipElement.getAttribute("value"));
        }
    }
}

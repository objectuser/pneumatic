package com.surgingsystems.etl.dsl.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.CompositeBeanDefinitionParser;
import com.surgingsystems.etl.filter.MapperFilter;

public class MapperFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private Logger logger = LogManager.getFormatterLogger(MapperFilterBeanDefinitionParser.class);

    private CompositeBeanDefinitionParser compositeBeanDefinitionParser = new CompositeBeanDefinitionParser();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MapperFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        compositeBeanDefinitionParser.parse(element, parserContext, bean, "input", "input");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "inputSchema", "inputSchema");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "output", "output");
        compositeBeanDefinitionParser.parse(element, parserContext, bean, "outputSchema", "outputSchema");

        Element mappingsElement = DomUtils.getChildElementByTagName(element, "mappings");
        if (mappingsElement != null) {

            ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                    parserContext.getDelegate(), bean.getBeanDefinition());
            
            MappingBeanDefinitionParser mappingBeanDefinitionParser = new MappingBeanDefinitionParser();
            BeanDefinition mappingBeanDefinition = mappingBeanDefinitionParser.parse(mappingsElement, nestedParserContext);
            bean.addPropertyValue("mapping", mappingBeanDefinition);
        }

        logger.trace("Building modifier (%s)", element.getAttribute("name"));
    }
}

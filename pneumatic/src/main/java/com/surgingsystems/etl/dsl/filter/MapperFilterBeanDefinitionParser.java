package com.surgingsystems.etl.dsl.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.MapperFilter;

public class MapperFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private Logger logger = LogManager.getFormatterLogger(MapperFilterBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MapperFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element inputElement = DomUtils.getChildElementByTagName(element, "input");
        bean.addPropertyReference("input", inputElement.getAttribute("ref"));

        Element inputSchemaElement = DomUtils.getChildElementByTagName(element, "inputSchema");
        bean.addPropertyReference("inputSchema", inputSchemaElement.getAttribute("ref"));

        Element outputElement = DomUtils.getChildElementByTagName(element, "output");
        bean.addPropertyReference("output", outputElement.getAttribute("ref"));

        Element outputSchemaElement = DomUtils.getChildElementByTagName(element, "outputSchema");
        bean.addPropertyReference("outputSchema", outputSchemaElement.getAttribute("ref"));

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

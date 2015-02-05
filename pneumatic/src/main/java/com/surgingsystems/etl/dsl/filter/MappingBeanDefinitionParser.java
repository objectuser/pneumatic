package com.surgingsystems.etl.dsl.filter;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.schema.ColumnBeanDefinitionParser;
import com.surgingsystems.etl.filter.mapping.Mapping;

public class MappingBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Mapping.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        ManagedMap<BeanDefinition, BeanDefinition> mappings = new ManagedMap<BeanDefinition, BeanDefinition>();
        List<Element> mappingElements = DomUtils.getChildElementsByTagName(element, "mapping");
        for (Element mappingElement : mappingElements) {

            ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                    parserContext.getDelegate(), bean.getBeanDefinition());
            
            Element fromElement = DomUtils.getChildElementByTagName(mappingElement, "from");
            BeanDefinition fromColumn = parseChildColumn(nestedParserContext, fromElement);
            Element toElement = DomUtils.getChildElementByTagName(mappingElement, "to");
            BeanDefinition toColumn = parseChildColumn(nestedParserContext, toElement);
            mappings.put(toColumn, fromColumn);
        }
        
        bean.addPropertyValue("columnDefinitionMappings", mappings);
    }

    private BeanDefinition parseChildColumn(ParserContext nestedParserContext, Element element) {
        Element columnElement = DomUtils.getChildElementByTagName(element, "column");
        BeanDefinition column = new ColumnBeanDefinitionParser().parse(columnElement, nestedParserContext);
        return column;
    }
}

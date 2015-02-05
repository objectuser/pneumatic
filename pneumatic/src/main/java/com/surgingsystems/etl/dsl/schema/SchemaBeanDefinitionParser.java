package com.surgingsystems.etl.dsl.schema;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.schema.TabularSchema;

public class SchemaBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return TabularSchema.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        // We want any parsing to occur as a child of this tag so we need to
        // make a new one that has this as it's owner/parent
        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        List<Element> columnElements = DomUtils.getChildElementsByTagName(element, "column");
        ManagedList<Object> columnDefinitions = new ManagedList<Object>();
        for (Element columnElement : columnElements) {
            ColumnBeanDefinitionParser parser = new ColumnBeanDefinitionParser();
            columnDefinitions.add(parser.parse(columnElement, nestedParserContext));
        }

        bean.addPropertyValue("columnDefinitions", columnDefinitions);
    }
}

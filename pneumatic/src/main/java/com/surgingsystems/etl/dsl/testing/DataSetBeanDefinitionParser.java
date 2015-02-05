package com.surgingsystems.etl.dsl.testing;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.surgingsystems.etl.testing.TestDataSet;

public class DataSetBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return TestDataSet.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        String schemaRef = element.getAttribute("schema-ref");
        bean.addPropertyReference("schema", schemaRef);

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());
        
        RecordFactoryBeanDefinitionParser recordFactoryBeanDefinitionParser = new RecordFactoryBeanDefinitionParser();
        BeanDefinition rfb = recordFactoryBeanDefinitionParser.parse(element, nestedParserContext);
        
        bean.addPropertyValue("recordFactory", rfb);
    }
}

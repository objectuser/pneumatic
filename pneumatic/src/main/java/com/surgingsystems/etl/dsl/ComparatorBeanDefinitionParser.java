package com.surgingsystems.etl.dsl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.schema.ColumnBeanDefinitionParser;
import com.surgingsystems.etl.record.SingleColumnComparator;

public class ComparatorBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(ComparatorBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return SingleColumnComparator.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        
        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());
        
        Element columnElement = DomUtils.getChildElementByTagName(element, "column");
        ColumnBeanDefinitionParser parser = new ColumnBeanDefinitionParser();
        BeanDefinition columnDefintion = parser.parse(columnElement, nestedParserContext);
        bean.addPropertyValue("columnDefinition", columnDefintion);

        logger.trace("Creating comparator on column");
    }
}

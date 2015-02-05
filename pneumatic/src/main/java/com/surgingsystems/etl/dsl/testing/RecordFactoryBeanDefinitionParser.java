package com.surgingsystems.etl.dsl.testing;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.filter.FileBeanDefinitionParser;

public class RecordFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return RecordFactory.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        Element fileResourceElement = DomUtils.getChildElementByTagName(element, "fileResource");
        if (fileResourceElement != null) {

            bean.getBeanDefinition().setBeanClassName(FileRecordFactory.class.getName());
            
            FileBeanDefinitionParser parser = new FileBeanDefinitionParser();
            BeanDefinition fileReader = parser.parse(fileResourceElement, nestedParserContext);
            
            bean.addPropertyValue("flatFileRecordReader", fileReader);
            
        } else {

            bean.getBeanDefinition().setBeanClassName(ListRecordFactory.class.getName());
            
            ManagedList<BeanDefinition> recordBeans = new ManagedList<BeanDefinition>();
            List<Element> recordElements = DomUtils.getChildElementsByTagName(element, "record");
            for (Element recordElement : recordElements) {
                RecordBeanDefinitionParser parser = new RecordBeanDefinitionParser();
                BeanDefinition recordBean = parser.parse(recordElement, nestedParserContext);
                recordBeans.add(recordBean);
            }
            
            bean.addPropertyValue("records", recordBeans);
        }
    }
}

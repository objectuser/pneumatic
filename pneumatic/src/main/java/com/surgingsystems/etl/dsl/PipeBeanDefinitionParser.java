package com.surgingsystems.etl.dsl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.surgingsystems.etl.pipe.BlockingQueuePipe;

public class PipeBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    
    private static Logger logger = LogManager.getFormatterLogger(PipeBeanDefinitionParser.class);
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return BlockingQueuePipe.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        logger.trace("Building pipe");
    }
}

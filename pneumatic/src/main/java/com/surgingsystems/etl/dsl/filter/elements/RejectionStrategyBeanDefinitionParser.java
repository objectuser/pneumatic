package com.surgingsystems.etl.dsl.filter.elements;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.PipeRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;

public class RejectionStrategyBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(RejectionStrategyBeanDefinitionParser.class);

    @Override
    protected String getBeanClassName(Element element) {
        return RejectRecordStrategy.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        Element outputConditionElement = DomUtils.getChildElementByTagName(element, "output");
        if (outputConditionElement != null) {
            logger.trace("Rejection strategy set to output pipe");
            bean.getBeanDefinition().setBeanClass(PipeRejectRecordStrategy.class);
            bean.addPropertyValue("output", outputConditionElement.getTextContent());
        }

        Element logElement = DomUtils.getChildElementByTagName(element, "log");
        if (logElement != null) {
            bean.getBeanDefinition().setBeanClass(LogRejectRecordStrategy.class);
            String logName = logElement.getAttribute("name");
            bean.addPropertyValue("loggerName", logName);
            String logLevel = logElement.getAttribute("level");
            Level level = Level.getLevel(logLevel);
            bean.addPropertyValue("level", level);
            logger.trace(String
                    .format("Rejection strategy set to log with name (%s) and level (%s)", logName, logLevel));
        }
    }
}

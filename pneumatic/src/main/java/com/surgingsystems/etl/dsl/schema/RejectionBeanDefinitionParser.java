package com.surgingsystems.etl.dsl.schema;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.mapping.LogRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.PipeRejectRecordStrategy;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;

public class RejectionBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return RejectRecordStrategy.class.getName();
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        Element outputElement = DomUtils.getChildElementByTagName(element, "output");
        if (outputElement != null) {
            bean.getBeanDefinition().setBeanClass(PipeRejectRecordStrategy.class);
            String outputPipe = outputElement.getAttribute("ref");
            bean.addPropertyReference("pipe", outputPipe);
        }

        Element logElement = DomUtils.getChildElementByTagName(element, "log");
        if (logElement != null) {
            bean.getBeanDefinition().setBeanClass(LogRejectRecordStrategy.class);

            String level = outputElement.getAttribute("level");
            if (!StringUtils.isEmpty(level)) {
                bean.addPropertyReference("level", level);
            }

            String loggerName = outputElement.getAttribute("name");
            if (StringUtils.isEmpty(loggerName)) {
                bean.addPropertyReference("loggerName", loggerName);
            }
        }
    }
}

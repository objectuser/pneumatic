package com.surgingsystems.etl.dsl.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.filter.FlatFileRecordReader;

public class FileBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LogManager.getFormatterLogger(FileBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return FlatFileRecordReader.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        String location = element.getAttribute("location");
        String locationExpression = element.getAttribute("locationExpression");
        if (!StringUtils.isEmpty(location)) {
            bean.addPropertyValue("resource", location);
        } else if (!StringUtils.isEmpty(locationExpression)) {
            bean.addPropertyValue("resourceExpression", locationExpression);
        } else {
            throw new IllegalArgumentException("The element must provide either a location or a locationExpression");
        }

        logger.trace("Creating file for resource location %s", location);

        Element skipElement = DomUtils.getChildElementByTagName(element, "skipLines");
        if (skipElement != null) {
            bean.addPropertyValue("linesToSkip", skipElement.getAttribute("value"));
        }

        Element filterLinesElement = DomUtils.getChildElementByTagName(element, "selectLines");
        if (filterLinesElement != null) {
            String selection = filterLinesElement.getAttribute("value");
            String[] rangeValues = selection.split("-");
            int from = Integer.parseInt(rangeValues[0]);
            int to = Integer.parseInt(rangeValues[1]);
            bean.addPropertyValue("lineRange", new Range(from, to));
        }
    }
}

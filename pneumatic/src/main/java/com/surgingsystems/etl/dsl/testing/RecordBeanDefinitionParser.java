package com.surgingsystems.etl.dsl.testing;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.record.DataRecord;

public class RecordBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return DataRecord.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {

        ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                parserContext.getDelegate(), bean.getBeanDefinition());

        ManagedList<BeanDefinition> columnValueBeans = new ManagedList<BeanDefinition>();
        List<Element> columnValueElements = DomUtils.getChildElementsByTagName(element, "columnValue");
        for (Element columnValueElement : columnValueElements) {

            ColumnValueBeanDefinitionParser columnValueBeanDefinitionParser = new ColumnValueBeanDefinitionParser();
            BeanDefinition columnValueBeanDefinition = columnValueBeanDefinitionParser.parse(columnValueElement,
                    nestedParserContext);
            columnValueBeans.add(columnValueBeanDefinition);
        }

        bean.addPropertyValue("columns", columnValueBeans);
    }
}

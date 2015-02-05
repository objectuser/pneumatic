package com.surgingsystems.etl.dsl.filter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.filter.database.ConfigurableDatabaseWriteStrategyBeanDefinitionParser;
import com.surgingsystems.etl.filter.DatabaseWriterFilter;
import com.surgingsystems.etl.filter.database.InsertDatabaseWriteStrategy;

public class DatabaseWriterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return DatabaseWriterFilter.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("name", element.getAttribute("name"));

        Element inputElement = DomUtils.getChildElementByTagName(element, "input");
        bean.addPropertyReference("input", inputElement.getAttribute("ref"));

        Element inputSchemaElement = DomUtils.getChildElementByTagName(element, "inputSchema");
        bean.addPropertyReference("inputSchema", inputSchemaElement.getAttribute("ref"));

        Element dataSourceElement = DomUtils.getChildElementByTagName(element, "dataSource");
        bean.addPropertyReference("dataSource", dataSourceElement.getAttribute("ref"));

        Element insertIntoElement = DomUtils.getChildElementByTagName(element, "insertInto");
        if (insertIntoElement != null) {
            InsertDatabaseWriteStrategy insertDatabaseWriteStrategy = new InsertDatabaseWriteStrategy();
            insertDatabaseWriteStrategy.setTableName(insertIntoElement.getAttribute("table-name"));
            bean.addPropertyValue("writeStrategy", insertDatabaseWriteStrategy);
        }

        Element sqlElement = DomUtils.getChildElementByTagName(element, "updateWith");
        if (sqlElement != null) {
            ParserContext nestedParserContext = new ParserContext(parserContext.getReaderContext(),
                    parserContext.getDelegate(), bean.getBeanDefinition());

            ConfigurableDatabaseWriteStrategyBeanDefinitionParser parser = new ConfigurableDatabaseWriteStrategyBeanDefinitionParser();
            BeanDefinition beanDefinition = parser.parse(sqlElement, nestedParserContext);

            bean.addPropertyValue("writeStrategy", beanDefinition);
        }
    }
}

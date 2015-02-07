package com.surgingsystems.etl.dsl;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.surgingsystems.etl.dsl.filter.AggregatorBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.CopyBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.DatabaseLookupBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.DatabaseReaderBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.DatabaseWriterBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.FileReaderFilterBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.FileWriterFilterBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.FunnelBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.JoinBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.MapperFilterBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.RestfulLookupBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.SortFilterBeanDefinitionParser;
import com.surgingsystems.etl.dsl.filter.TransformerBeanDefinitionParser;
import com.surgingsystems.etl.dsl.schema.ColumnBeanDefinitionParser;
import com.surgingsystems.etl.dsl.schema.SchemaBeanDefinitionParser;
import com.surgingsystems.etl.dsl.testing.DataSetBeanDefinitionParser;
import com.surgingsystems.etl.dsl.testing.EtlTestBeanDefinitionParser;

public class EtlNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("aggregator", new AggregatorBeanDefinitionParser());
        registerBeanDefinitionParser("copy", new CopyBeanDefinitionParser());
        registerBeanDefinitionParser("databaseLookup", new DatabaseLookupBeanDefinitionParser());
        registerBeanDefinitionParser("databaseReader", new DatabaseReaderBeanDefinitionParser());
        registerBeanDefinitionParser("databaseWriter", new DatabaseWriterBeanDefinitionParser());
        registerBeanDefinitionParser("fileReader", new FileReaderFilterBeanDefinitionParser());
        registerBeanDefinitionParser("fileWriter", new FileWriterFilterBeanDefinitionParser());
        registerBeanDefinitionParser("funnel", new FunnelBeanDefinitionParser());
        registerBeanDefinitionParser("join", new JoinBeanDefinitionParser());
        registerBeanDefinitionParser("mapper", new MapperFilterBeanDefinitionParser());
        registerBeanDefinitionParser("pipe", new PipeBeanDefinitionParser());
        registerBeanDefinitionParser("sort", new SortFilterBeanDefinitionParser());
        registerBeanDefinitionParser("restfulLookup", new RestfulLookupBeanDefinitionParser());
        registerBeanDefinitionParser("transformer", new TransformerBeanDefinitionParser());

        registerBeanDefinitionParser("schema", new SchemaBeanDefinitionParser());
        registerBeanDefinitionParser("column", new ColumnBeanDefinitionParser());
        
        registerBeanDefinitionParser("dataSet", new DataSetBeanDefinitionParser());
        registerBeanDefinitionParser("test", new EtlTestBeanDefinitionParser());
    }
}

package com.surgingsystems.etl.yamldsl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

import com.surgingsystems.etl.filter.AggregatorFilter;
import com.surgingsystems.etl.filter.CopyFilter;
import com.surgingsystems.etl.filter.DatabaseLookupFilter;
import com.surgingsystems.etl.filter.DatabaseReaderFilter;
import com.surgingsystems.etl.filter.DatabaseWriterFilter;
import com.surgingsystems.etl.filter.FileReaderFilter;
import com.surgingsystems.etl.filter.FileWriterFilter;
import com.surgingsystems.etl.filter.FlatFileRecordReader;
import com.surgingsystems.etl.filter.FunnelFilter;
import com.surgingsystems.etl.filter.JoinFilter;
import com.surgingsystems.etl.filter.MapperFilter;
import com.surgingsystems.etl.filter.RestfulListenerFilter;
import com.surgingsystems.etl.filter.RestfulLookupFilter;
import com.surgingsystems.etl.filter.RestfulWriterFilter;
import com.surgingsystems.etl.filter.SortFilter;
import com.surgingsystems.etl.filter.SplitterFilter;
import com.surgingsystems.etl.filter.TransformerFilter;
import com.surgingsystems.etl.filter.database.ConfigurableDatabaseWriteStrategy;
import com.surgingsystems.etl.filter.function.AverageFunction;
import com.surgingsystems.etl.filter.function.CounterFunction;
import com.surgingsystems.etl.filter.function.Function;
import com.surgingsystems.etl.filter.function.SumFunction;
import com.surgingsystems.etl.filter.mapping.RejectRecordStrategy;
import com.surgingsystems.etl.filter.transformer.OutputCondition;
import com.surgingsystems.etl.filter.transformer.TransformerFilterOutputConfiguration;
import com.surgingsystems.etl.pipe.BlockingQueuePipe;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.SingleColumnComparator;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.TabularSchema;

@Component
public class YamlParser {

    private static Logger logger = LogManager.getFormatterLogger(YamlParser.class);

    @SuppressWarnings("serial")
    private static Map<String, Class<?>> nameToTypeMap = new LinkedHashMap<String, Class<?>>() {
        {
            put("!aggregator", AggregatorFilter.class);
            put("!copy", CopyFilter.class);
            put("!databaseLookup", DatabaseLookupFilter.class);
            put("!databaseReader", DatabaseReaderFilter.class);
            put("!databaseWriter", DatabaseWriterFilter.class);
            put("!fileReader", FileReaderFilter.class);
            put("!fileWriter", FileWriterFilter.class);
            put("!funnel", FunnelFilter.class);
            put("!join", JoinFilter.class);
            put("!mapper", MapperFilter.class);
            put("!restfulListener", RestfulListenerFilter.class);
            put("!restfulLookup", RestfulLookupFilter.class);
            put("!restfulWriter", RestfulWriterFilter.class);
            put("!splitter", SplitterFilter.class);
            put("!sort", SortFilter.class);
            put("!transformer", TransformerFilter.class);

            put("!pipe", BlockingQueuePipe.class);
            put("!schema", TabularSchema.class);
            put("!average", AverageFunction.class);
            put("!count", CounterFunction.class);
            put("!sum", SumFunction.class);
        }
    };

    @SuppressWarnings("serial")
    private static Map<String, Class<?>> conventionNameToTypeMap = new LinkedHashMap<String, Class<?>>() {
        {
            put("Aggregator", AggregatorFilter.class);
            put("Copy", CopyFilter.class);
            put("DatabaseLookup", DatabaseLookupFilter.class);
            put("DatabaseReader", DatabaseReaderFilter.class);
            put("DatabaseWriter", DatabaseWriterFilter.class);
            put("FileReader", FileReaderFilter.class);
            put("FileWriter", FileWriterFilter.class);
            put("Funnel", FunnelFilter.class);
            put("Join", JoinFilter.class);
            put("Mapper", MapperFilter.class);
            put("RestfulListener", RestfulListenerFilter.class);
            put("RestfulLookup", RestfulLookupFilter.class);
            put("RestfulWriter", RestfulWriterFilter.class);
            put("Splitter", SplitterFilter.class);
            put("Sort", SortFilter.class);
            put("Transformer", TransformerFilter.class);

            put("Pipe", BlockingQueuePipe.class);
            put("Input", BlockingQueuePipe.class);
            put("Output", BlockingQueuePipe.class);
            put("Schema", TabularSchema.class);

            put("Average", AverageFunction.class);
            put("Count", CounterFunction.class);
            put("Sum", SumFunction.class);
        }
    };

    @SuppressWarnings("serial")
    private static Map<Class<?>, Map<String, Object>> typeToBeanMap = new LinkedHashMap<Class<?>, Map<String, Object>>() {
        {
            put(AggregatorFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("function", Function.class);
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(CopyFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("outputs", new ArrayList<Class<?>>() {
                        {
                            add(Pipe.class);
                        }
                    });
                }
            });
            put(DatabaseLookupFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("dataSource", DataSource.class);
                    put("sql", String.class);
                    put("parameters", new ArrayList<Class<?>>() {
                        {
                            add(String.class);
                        }
                    });
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(DatabaseReaderFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("dataSource", DataSource.class);
                    put("sql", String.class);
                    put("parameters", new ArrayList<Class<?>>() {
                        {
                            add(String.class);
                        }
                    });
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(DatabaseWriterFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                    put("dataSource", DataSource.class);
                    put("insertInto", new DatabaseInsertAdapter());
                    put("updateWith", new DatabaseUpdateAdapter());
                }
            });
            put(FileReaderFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("fileResource", new FlatFileRecordReaderAdapter("itemReader"));
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(FileWriterFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("fileResource", new ResourceAdapter());
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                }
            });
            put(FunnelFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("inputs", new ArrayList<Class<?>>() {
                        {
                            add(Pipe.class);
                        }
                    });
                    put("output", Pipe.class);
                }
            });
            put(JoinFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("leftInput", Pipe.class);
                    put("rightInput", Pipe.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("comparator", new ComparatorAdapter("comparator"));
                    put("leftOuterJoin", String.class);
                }
            });
            put(MapperFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("mappings", new MappingsAdapter());
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(RestfulLookupFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("httpMethod", new HttpMethodAdapter());
                    put("requestUrl", String.class);
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("responseSchema", TabularSchema.class);
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(RestfulListenerFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("path", String.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                }
            });
            put(RestfulWriterFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("httpMethod", new HttpMethodAdapter());
                    put("url", String.class);
                    put("input", Pipe.class);
                    put("inputSchema", TabularSchema.class);
                    put("rejection", new RejectRecordStrategyAdapter());
                }
            });
            put(SplitterFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("outputConditions", new ArrayList<Class<?>>() {
                        {
                            add(OutputCondition.class);
                        }
                    });
                }
            });
            put(SortFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("output", Pipe.class);
                    put("comparator", new ComparatorAdapter("comparator"));
                }
            });
            put(TransformerFilter.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("input", Pipe.class);
                    put("variables", new LinkedHashMap<Class<?>, Class<?>>() {
                        {
                            put(String.class, String.class);
                        }
                    });
                    put("expressions", new ArrayList<Class<?>>() {
                        {
                            add(String.class);
                        }
                    });
                    put("outputConfigurations", new ArrayList<Class<?>>() {
                        {
                            add(TransformerFilterOutputConfiguration.class);
                        }
                    });
                }
            });

            put(SingleColumnComparator.class, new LinkedHashMap<String, Object>() {
                {
                    put("columnDefinition", new ColumnLikePropertyAdapter("columnDefinition"));
                }
            });

            put(FlatFileRecordReader.class, new LinkedHashMap<String, Object>() {
                {
                    put("location", new PropertyNameAdapter("resource"));
                    put("linesToSkip", String.class);
                }
            });

            put(ConfigurableDatabaseWriteStrategy.class, new LinkedHashMap<String, Object>() {
                {
                    put("sql", String.class);
                    put("parameters", new ArrayList<Class<?>>() {
                        {
                            add(String.class);
                        }
                    });
                }
            });

            put(BlockingQueuePipe.class, new LinkedHashMap<String, Object>());

            put(TabularSchema.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("columns", new ArrayList<Class<?>>() {
                        {
                            add(ColumnDefinition.class);
                        }
                    });
                }
            });
            put(ColumnDefinition.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("type", new ColumnTypeAdapter());
                }
            });
            put(ColumnMappingAdapter.class, new LinkedHashMap<String, Object>() {
                {
                    put("from", new ColumnLikePropertyAdapter("from"));
                    put("to", new ColumnLikePropertyAdapter("to"));
                }
            });

            put(AverageFunction.class, new LinkedHashMap<String, Object>() {
                {
                    put("in", new ColumnLikePropertyAdapter("inputColumnDefinition"));
                    put("out", new ColumnLikePropertyAdapter("outputColumnDefinition"));
                }
            });
            put(CounterFunction.class, new LinkedHashMap<String, Object>() {
                {
                    put("out", new ColumnLikePropertyAdapter("outputColumnDefinition"));
                }
            });
            put(SumFunction.class, new LinkedHashMap<String, Object>() {
                {
                    put("in", new ColumnLikePropertyAdapter("inputColumnDefinition"));
                    put("out", new SumOutputColumnAdapter("outputColumnDefinition"));
                }
            });

            put(TransformerFilterOutputConfiguration.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("output", Pipe.class);
                    put("outputSchema", TabularSchema.class);
                    put("recordName", String.class);
                    put("outputCondition", String.class);
                    put("variables", new LinkedHashMap<Class<?>, Class<?>>() {
                        {
                            put(String.class, String.class);
                        }
                    });
                    put("expressions", new ArrayList<Class<?>>() {
                        {
                            add(String.class);
                        }
                    });
                }
            });

            put(OutputCondition.class, new LinkedHashMap<String, Object>() {
                {
                    put("name", String.class);
                    put("output", Pipe.class);
                    put("outputCondition", String.class);
                }
            });

            put(RejectRecordStrategy.class, new LinkedHashMap<String, Object>() {
                {
                    put("output", Pipe.class);
                }
            });
        }
    };

    @Autowired
    private GenericApplicationContext applicationContext;

    public void parse(String yamlFile) {
        try {
            Yaml yaml = new Yaml();
            Reader reader = readFileAsReader(yamlFile);
            MappingNode node = (MappingNode) yaml.compose(reader);
            for (NodeTuple nodeTuple : node.getValue()) {
                ScalarNode key = (ScalarNode) nodeTuple.getKeyNode();
                String beanIdentifier = key.getValue();
                logger.debug("Building bean named %s", beanIdentifier);

                BeanDefinition beanDefinition = createBeanDefinition(beanIdentifier, nodeTuple.getValueNode());
                applicationContext.registerBeanDefinition(beanIdentifier, beanDefinition);
            }
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Error parsing '%s'", yamlFile), exception);
        }
    }

    private Reader readFileAsReader(String path) throws IOException {
        Resource resource = applicationContext.getResource(path);
        return new BufferedReader(new FileReader(resource.getFile()));
    }

    private Class<?> determineTypeForBean(String name, String declaredType) {
        Class<?> result = nameToTypeMap.get(declaredType);
        logger.debug("Declared type %s not found", declaredType);
        if (result != null) {
            return result;
        } else {
            for (Map.Entry<String, Class<?>> entry : conventionNameToTypeMap.entrySet()) {
                if (name.toLowerCase().endsWith(entry.getKey().toLowerCase())) {
                    return entry.getValue();
                }
            }

            throw new IllegalArgumentException(String.format("Unable to determine type for (%s)", name));
        }

    }

    private BeanDefinition createBeanDefinition(String beanIdentifier, Node node) {

        Tag tag = node.getTag();
        String declaredType = tag.getValue();
        logger.debug("Declared type %s", declaredType);

        Class<?> type = determineTypeForBean(beanIdentifier, declaredType);

        return createBeanOfType(type, node);
    }

    private BeanDefinition createBeanOfType(Class<?> type, Node node) {
        BeanDefinitionBuilder builder = buildBeanOfType(type, node);
        return builder.getBeanDefinition();
    }

    private BeanDefinitionBuilder buildBeanOfType(Class<?> type, Node node) {
        Map<String, Object> beanMap = typeToBeanMap.get(type);
        logger.debug("Bean type is %s", type.getSimpleName());

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type);

        if (node instanceof MappingNode) {
            MappingNode mappingNode = (MappingNode) node;
            mapBean(builder, beanMap, mappingNode);
        } else {
            // a simple mapping like a pipe.
        }

        return builder;
    }

    /**
     * Map the properties from the node to the builder using the map.
     */
    @SuppressWarnings("unchecked")
    private void mapBean(BeanDefinitionBuilder builder, Map<String, Object> beanMap, MappingNode mappingNode) {
        for (NodeTuple nodeTuple : mappingNode.getValue()) {
            ScalarNode key = (ScalarNode) nodeTuple.getKeyNode();
            String propertyName = key.getValue();
            Node nodeValue = nodeTuple.getValueNode();
            Object target = beanMap.get(propertyName);
            if (isTypeTagged(nodeValue)) {
                BeanDefinition beanDefinition = createBeanDefinition(propertyName, nodeValue);
                builder.addPropertyValue(propertyName, beanDefinition);
            } else if (target instanceof List) {
                List<Class<?>> typeList = (List<Class<?>>) target;
                Class<?> targetType = typeList.get(0);
                ManagedList<Object> list = createListOf(targetType, nodeValue);
                builder.addPropertyValue(propertyName, list);
            } else if (target instanceof Map) {
                ManagedMap<String, Object> map = createMapFrom(nodeValue);
                builder.addPropertyValue(propertyName, map);
            } else if (target instanceof YamlAdapter) {
                YamlAdapter adapter = (YamlAdapter) target;
                if (nodeValue instanceof ScalarNode) {
                    ScalarNode scalarNode = (ScalarNode) nodeValue;
                    adapter.adapt(builder, propertyName, scalarNode.getValue(), applicationContext);
                } else if (nodeValue instanceof SequenceNode) {
                    ManagedList<Object> list = createListOf(adapter.getTargetType(), nodeValue);
                    adapter.adapt(builder, propertyName, list, applicationContext);
                } else {
                    BeanDefinitionBuilder valueBuilder = buildBeanOfType(adapter.getTargetType(), nodeValue);
                    adapter.adaptBuilder(builder, propertyName, valueBuilder, applicationContext);
                }
            } else if (isSimpleValue(target, nodeValue)) {
                ScalarNode scalarNode = (ScalarNode) nodeValue;
                builder.addPropertyValue(propertyName, buildValue(scalarNode.getValue()));
            } else if (isRefValue(target, nodeValue)) {
                ScalarNode scalarNode = (ScalarNode) nodeValue;
                builder.addPropertyValue(propertyName, buildRef(scalarNode.getValue()));
            } else if (target instanceof Class) {
                Class<?> targetType = (Class<?>) target;
                BeanDefinition beanDefinition = createBeanOfType(targetType, nodeValue);
                builder.addPropertyValue(propertyName, beanDefinition);
            } else {
                throw new IllegalArgumentException(String.format("Key (%s) is unknown for type %s", propertyName,
                        builder.getRawBeanDefinition().getBeanClassName()));
            }
        }
    }

    /**
     * Is the value a string?
     */
    private boolean isSimpleValue(Object target, Node node) {
        return node instanceof ScalarNode && String.class.equals(target);
    }

    /**
     * Is the value a reference?
     */
    private boolean isRefValue(Object target, Node node) {
        return node instanceof ScalarNode && target instanceof Class;
    }

    private boolean isTypeTagged(Node node) {
        Tag tag = node.getTag();
        return nameToTypeMap.containsKey(tag.getValue());
    }

    /**
     * Create a list of the given type, either a string or another mapped type
     */
    private ManagedList<Object> createListOf(Class<?> targetType, Node node) {
        SequenceNode sequenceNode = (SequenceNode) node;
        ManagedList<Object> list = new ManagedList<>();
        for (Node value : sequenceNode.getValue()) {
            if (isSimpleValue(targetType, value)) {
                ScalarNode scalarNode = (ScalarNode) value;
                list.add(buildValue(scalarNode.getValue()));
            } else if (isRefValue(targetType, value)) {
                ScalarNode scalarNode = (ScalarNode) value;
                list.add(buildRef(scalarNode.getValue()));
            } else {
                BeanDefinition beanDefinition = createBeanOfType(targetType, value);
                list.add(beanDefinition);
            }
        }
        return list;
    }

    /**
     * Create a map of strings
     */
    private ManagedMap<String, Object> createMapFrom(Node node) {
        MappingNode mappingNode = (MappingNode) node;
        ManagedMap<String, Object> map = new ManagedMap<>();
        for (NodeTuple nodeTuple : mappingNode.getValue()) {
            ScalarNode key = (ScalarNode) nodeTuple.getKeyNode();
            Node value = nodeTuple.getValueNode();
            if (value instanceof ScalarNode) {
                ScalarNode scalarNode = (ScalarNode) value;
                map.put(key.getValue(), buildValue(scalarNode.getValue()));
            } else if (value instanceof SequenceNode) {
                ManagedList<Object> list = createListOf(getClass(), value);
                map.put(key.getValue(), list);
            } else if (value instanceof MappingNode) {
                ManagedMap<String, Object> submap = createMapFrom(value);
                map.put(key.getValue(), submap);
            }
        }
        return map;
    }

    private Object buildValue(String value) {
        return value;
    }

    private Object buildRef(String value) {
        return new RuntimeBeanReference(value);
    }
}
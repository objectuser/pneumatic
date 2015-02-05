package com.surgingsystems.etl.test.builder;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.StringColumnType;

public class SchemaBuilderFactoryBean implements FactoryBean<SchemaBuilder> {

    @Autowired
    private StringColumnType stringColumnType;

    @Autowired
    private IntegerColumnType integerColumnType;

    @Autowired
    private DecimalColumnType decimalColumnType;

    @Override
    public SchemaBuilder getObject() throws Exception {
        return SchemaBuilder.create();
    }

    @Override
    public Class<?> getObjectType() {
        return SchemaBuilder.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}

package com.surgingsystems.etl.filter.expression;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.surgingsystems.etl.dsl.filter.RecordPropertyAccessor;
import com.surgingsystems.etl.pipe.BlockingQueuePipe;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.DecimalColumnType;
import com.surgingsystems.etl.schema.IntegerColumnType;
import com.surgingsystems.etl.schema.StringColumnType;
import com.surgingsystems.etl.schema.TabularSchema;

public class TransformerBeanResolverTest {
    
    private Pipe in1 = new BlockingQueuePipe();
    
    private Pipe out1 = new BlockingQueuePipe();
    
    private TabularSchema outputSchema = new TabularSchema("Output Schema");
    
    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    
    private DataRecord inRecord;
    
    private DataRecord outRecord;
    
    @Before
    public void setup() {
        
        evaluationContext.addPropertyAccessor(new RecordPropertyAccessor());
        
        inRecord = new DataRecord();
        inRecord.addColumn(new Column<String>(new ColumnDefinition<String>("Name", new StringColumnType()), "Fred"));
        inRecord.addColumn(new Column<Integer>(new ColumnDefinition<Integer>("Count", new IntegerColumnType()), 1));
        inRecord.addColumn(new Column<Double>(new ColumnDefinition<Double>("Price", new DecimalColumnType()), new Double(100.0)));
        
        outRecord = new DataRecord();
        outRecord.addColumn(new Column<String>(new ColumnDefinition<String>("Name", new StringColumnType()), null));
        outRecord.addColumn(new Column<Integer>(new ColumnDefinition<Integer>("Count", new IntegerColumnType()), null));
        outRecord.addColumn(new Column<Double>(new ColumnDefinition<Double>("Price", new DecimalColumnType()), null));
        
        evaluationContext.setVariable("in1", in1);
        evaluationContext.setVariable("out1", out1);
        evaluationContext.setVariable("inputRecord", inRecord);
        evaluationContext.setVariable("outputRecord", outRecord);
        
        outputSchema.addColumnDefinition(new ColumnDefinition<String>("Name", new StringColumnType()));
        outputSchema.addColumnDefinition(new ColumnDefinition<Integer>("Count", new IntegerColumnType()));
        outputSchema.addColumnDefinition(new ColumnDefinition<Double>("Price", new DecimalColumnType()));
    }
    
    @Test
    public void resolveBean() {
        SpelExpressionParser expressionParser = new SpelExpressionParser();
        Object name = expressionParser.parseExpression("#outputRecord.Name = #inputRecord.Name + #inputRecord.Name").getValue(evaluationContext);
        Object count = expressionParser.parseExpression("#outputRecord.Count = #inputRecord.Count + #inputRecord.Count").getValue(evaluationContext);
        Object price = expressionParser.parseExpression("#outputRecord.Price = #inputRecord.Price + #inputRecord.Price").getValue(evaluationContext);
        Assert.assertNotNull(name);
        Assert.assertNotNull(count);
        Assert.assertNotNull(price);

        Assert.assertEquals("FredFred", outRecord.getColumnForName("Name").getValue());
        Assert.assertEquals(2, outRecord.getColumnForName("Count").getValue());
        Assert.assertEquals(200.0, outRecord.getColumnForName("Price").getValue());
    }
}

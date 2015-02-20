package com.surgingsystems.etl.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;
import com.surgingsystems.etl.test.filter.pipe.PipeUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:etl-context.xml", "join-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JoinFilterTest {

    @Resource(name = "innerJoin")
    private JoinFilter innerJoinFilter;

    @Resource(name = "outerJoin")
    private JoinFilter outerJoinFilter;

    @Resource(name = "innerJoinWithCustomComparator")
    private JoinFilter innerJoinWithCustomComparator;

    @Resource(name = "input1")
    private Pipe input1;

    @Resource(name = "input2")
    private Pipe input2;

    @Resource(name = "output")
    private Pipe output;

    @Resource(name = "inputSchema")
    private Schema inputSchema;

    @Test
    public void innerJoin() {
        DataRecord one = new DataRecord(inputSchema, "one", 1, 100.0);
        DataRecord two = new DataRecord(inputSchema, "two", 2, 200.0);
        DataRecord three = new DataRecord(inputSchema, "three", 3, 300.0);
        DataRecord four = new DataRecord(inputSchema, "four", 4, 400.0);
        DataRecord five = new DataRecord(inputSchema, "five", 5, 500.0);

        input1.put(one);
        input1.put(five);
        input1.put(three);
        input1.put(two);

        input2.put(one);
        input2.put(four);
        input2.put(three);

        input1.closedForInput();
        input2.closedForInput();
        innerJoinFilter.run();

        List<Record> outputs = PipeUtility.toList(output);
        Assert.assertEquals("Output record count", 2, outputs.size());
        Assert.assertEquals("Records are correct", Arrays.asList(one, three), outputs);
    }

    @Test
    public void outerJoin() {
        DataRecord one = new DataRecord(inputSchema, "one", 1, 100.0);
        DataRecord two = new DataRecord(inputSchema, "two", 2, 200.0);
        DataRecord three = new DataRecord(inputSchema, "three", 3, 300.0);
        DataRecord four = new DataRecord(inputSchema, "four", 4, 400.0);
        DataRecord five = new DataRecord(inputSchema, "five", 5, 500.0);

        input1.put(one);
        input1.put(five);
        input1.put(three);
        input1.put(two);

        input2.put(one);
        input2.put(four);
        input2.put(three);

        input1.closedForInput();
        input2.closedForInput();
        outerJoinFilter.run();

        List<Record> outputs = PipeUtility.toList(output);
        Assert.assertEquals("Output record count", 4, outputs.size());
        Assert.assertEquals("Records are correct", Arrays.asList(one, five, three, two), outputs);
    }

    @Test
    public void innerJoinWithCustomComparator() {
        DataRecord one = new DataRecord(inputSchema, "one", 1, 100.0);
        DataRecord two = new DataRecord(inputSchema, "two", 2, 200.0);
        DataRecord three = new DataRecord(inputSchema, "three", 3, 300.0);
        DataRecord four = new DataRecord(inputSchema, "four", 4, 400.0);
        DataRecord five = new DataRecord(inputSchema, "five", 5, 500.0);

        input1.put(one);
        input1.put(five);
        input1.put(three);
        input1.put(two);

        input2.put(one);
        input2.put(four);
        input2.put(three);

        input1.closedForInput();
        input2.closedForInput();
        innerJoinWithCustomComparator.run();

        List<Record> outputs = PipeUtility.toList(output);
        Assert.assertEquals("Output record count", 2, outputs.size());
        Assert.assertEquals("Records are correct", Arrays.asList(one, three), outputs);
    }

    public static class TwoColumnComparator<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements
            Comparator<Record> {

        private List<ColumnDefinition<?>> columns = new ArrayList<ColumnDefinition<?>>();

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Record r1, Record r2) {
            if (r1 == null && r2 == null) {
                return 0;
            } else if (r1 == null && r2 != null) {
                return -1;
            } else if (r1 != null && r2 == null) {
                return 1;
            } else {
                ColumnDefinition<T1> cd1 = (ColumnDefinition<T1>) columns.get(0);
                ColumnDefinition<T2> cd2 = (ColumnDefinition<T2>) columns.get(1);
                Column<T1> r1c1 = r1.getColumnFor(cd1);
                Column<T2> r1c2 = r1.getColumnFor(cd2);
                Column<T1> r2c1 = r2.getColumnFor(cd1);
                Column<T2> r2c2 = r2.getColumnFor(cd2);

                int comparison1 = r1c1.compareTo(r2c1);
                if (comparison1 == 0) {
                    return r1c2.compareTo(r2c2);
                } else {
                    return comparison1;
                }
            }
        }

        public List<ColumnDefinition<?>> getColumns() {
            return columns;
        }

        public void setColumns(List<ColumnDefinition<?>> columns) {
            this.columns = columns;
        }
    }
}

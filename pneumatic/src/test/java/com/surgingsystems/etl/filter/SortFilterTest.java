package com.surgingsystems.etl.filter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration({ "classpath:etl-context.xml", "sort-filter-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SortFilterTest {

    @Resource(name = "sort")
    private SortFilter sortFilter;

    @Resource(name = "sortWithBean")
    private SortFilter sortWithBeanFilter;

    @Resource(name = "input")
    private Pipe input;

    @Resource(name = "output")
    private Pipe output;

    @Autowired
    private Schema schema;

    @Test(expected = IllegalArgumentException.class)
    public void inputIsRequired() {
        sortFilter.setInput(null);
        sortFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputIsRequired() {
        sortFilter.setOutput(null);
        sortFilter.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void comparatorIsRequired() {
        sortFilter.setComparator(null);
        sortFilter.validate();
    }

    @Test
    public void sort() {
        DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
        DataRecord bronson = new DataRecord(schema, "Bronson", 5, 5000.0);
        DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
        input.put(trance);
        input.put(bronson);
        input.put(mach6);
        input.closedForInput();
        sortFilter.run();
        List<Record> outputs = PipeUtility.toList(output);
        List<Record> expected = Arrays.asList(bronson, mach6, trance);
        Assert.assertEquals(expected, outputs);
    }

    @Test
    public void sortWithBean() {
        DataRecord trance = new DataRecord(schema, "Trance", 10, 2000.0);
        DataRecord bronson1 = new DataRecord(schema, "Bronson", 5, 5000.0);
        DataRecord bronson2 = new DataRecord(schema, "Bronson", 3, 7000.0);
        DataRecord mach6 = new DataRecord(schema, "Mach6", 15, 6000.0);
        input.put(trance);
        input.put(bronson2);
        input.put(bronson1);
        input.put(mach6);
        input.closedForInput();
        sortWithBeanFilter.run();
        List<Record> outputs = PipeUtility.toList(output);
        List<Record> expected = Arrays.asList(bronson1, bronson2, mach6, trance);
        Assert.assertEquals(expected, outputs);
    }

    public static class TwoColumnComparator<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements
            Comparator<Record> {

        private ColumnDefinition<T1> column1;

        private ColumnDefinition<T2> column2;

        @Override
        public int compare(Record r1, Record r2) {
            if (r1 == null && r2 == null) {
                return 0;
            } else if (r1 == null && r2 != null) {
                return -1;
            } else if (r1 != null && r2 == null) {
                return 1;
            } else {
                Column<T1> r1c1 = r1.getColumnFor(column1);
                Column<T2> r1c2 = r1.getColumnFor(column2);
                Column<T1> r2c1 = r2.getColumnFor(column1);
                Column<T2> r2c2 = r2.getColumnFor(column2);

                int comparison1 = r1c1.compareTo(r2c1);
                if (comparison1 == 0) {
                    return r1c2.compareTo(r2c2);
                } else {
                    return comparison1;
                }
            }
        }

        public ColumnDefinition<T1> getColumn1() {
            return column1;
        }

        public void setColumn1(ColumnDefinition<T1> column1) {
            this.column1 = column1;
        }

        public ColumnDefinition<T2> getColumn2() {
            return column2;
        }

        public void setColumn2(ColumnDefinition<T2> column2) {
            this.column2 = column2;
        }

    }
}

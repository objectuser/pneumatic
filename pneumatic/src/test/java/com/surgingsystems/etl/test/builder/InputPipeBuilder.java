package com.surgingsystems.etl.test.builder;

import java.util.ArrayList;
import java.util.List;

import com.surgingsystems.etl.pipe.BlockingQueuePipe;
import com.surgingsystems.etl.pipe.Pipe;
import com.surgingsystems.etl.record.DataRecord;
import com.surgingsystems.etl.schema.Column;
import com.surgingsystems.etl.schema.ColumnDefinition;
import com.surgingsystems.etl.schema.Schema;

public class InputPipeBuilder {

    private int rowCount = 3;

    private List<ColumnDefinition<? extends Comparable<?>>> columnDefinitions = new ArrayList<ColumnDefinition<? extends Comparable<?>>>();

    private int[] valueIndexes;

    private static String[] states = { "Alabama", //
            "Alaska", //
            "Arizona", //
            "Arkansas", //
            "California", //
            "Colorado", //
            "Connecticut", //
            "Delaware", //
            "Florida", //
            "Georgia", //
            "Hawaii", //
            "Idaho", //
            "Illinois", //
            "Indiana", //
            "Iowa", //
            "Kansas", //
            "Kentucky", //
            "Louisiana", //
            "Maine", //
            "Maryland", //
            "Massachusetts", //
            "Michigan", //
            "Minnesota", //
            "Mississippi", //
            "Missouri", //
            "Montana", //
            "Nebraska", //
            "Nevada", //
            "New Hampshire", //
            "New Jersey", //
            "New Mexico", //
            "New York", //
            "North Carolina", //
            "North Dakota", //
            "Ohio", //
            "Oklahoma", //
            "Oregon", //
            "Pennsylvania", //
            "Rhode Island", //
            "South Carolina", //
            "South Dakota", //
            "Tennessee", //
            "Texas", //
            "Utah", //
            "Vermont", //
            "Virginia", //
            "Washington", //
            "West Virginia", //
            "Wisconsin", //
            "Wyoming" //
    };

    public static InputPipeBuilder create() {
        return new InputPipeBuilder();
    }

    public String getValueAtIndex(int index) {
        return states[index];
    }

    public InputPipeBuilder withRowCount(int rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    public List<ColumnDefinition<? extends Comparable<?>>> getColumnDefinitions() {
        return columnDefinitions;
    }

    @SuppressWarnings("unchecked")
    public Pipe build() {

        Schema schema = SchemaBuilder.create().withDefaultDefinitions().build();
        columnDefinitions.addAll(schema.getColumns());

        Pipe input = new BlockingQueuePipe();

        for (int i = 0; i < rowCount; ++i) {
            DataRecord record = new DataRecord();
            int columnIndex = 0;
            record.addColumn(new Column<String>((ColumnDefinition<String>) columnDefinitions.get(columnIndex++),
                    states[valueIndexes[i]]));
            record.addColumn(new Column<Integer>((ColumnDefinition<Integer>) columnDefinitions.get(columnIndex++), i));
            record.addColumn(new Column<Double>(
                    (ColumnDefinition<Double>) columnDefinitions.get(columnIndex++), i * 100.0));
            input.put(record);
        }

        return input;
    }

    public InputPipeBuilder withValueIndexes(int... valueIndexes) {
        this.valueIndexes = valueIndexes;
        return this;
    }

}

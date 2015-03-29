package com.surgingsystems.etl.filter.transformer;

import com.surgingsystems.etl.pipe.Pipe;

public abstract class ConditionalOutputConfiguration {

    private String name;

    private Pipe output;

    private String outputCondition;

    public void close() {
        output.closedForInput();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pipe getOutput() {
        return output;
    }

    public void setOutput(Pipe output) {
        this.output = output;
    }

    public String getOutputCondition() {
        return outputCondition;
    }

    public void setOutputCondition(String outputCondition) {
        this.outputCondition = outputCondition;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getName());
    }
}

package com.surgingsystems.etl.pipe;

import com.surgingsystems.etl.filter.InputFilter;
import com.surgingsystems.etl.filter.OutputFilter;

/**
 * Builder for connecting a pipe to an input and an output.
 */
public class PipeConnector {

    private Pipe pipe;

    public static PipeConnector connectPipe(Pipe pipe) {
        return new PipeConnector(pipe);
    }

    public PipeConnector toOutputOf(OutputFilter output) {
        output.addOutput(pipe);
        return this;
    }

    public PipeConnector toInputOf(InputFilter input) {
        input.addInput(pipe);
        return this;
    }

    private PipeConnector(Pipe pipe) {
        this.pipe = pipe;
    }

}

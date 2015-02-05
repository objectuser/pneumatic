package com.surgingsystems.etl.filter;

import com.surgingsystems.etl.pipe.Pipe;

public interface OutputFilter extends Filter {

    void addOutput(Pipe pipe);
}

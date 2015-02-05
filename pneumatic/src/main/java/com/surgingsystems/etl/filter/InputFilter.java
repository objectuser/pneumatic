package com.surgingsystems.etl.filter;

import com.surgingsystems.etl.pipe.Pipe;

public interface InputFilter extends Filter {

    void addInput(Pipe pipe);
}

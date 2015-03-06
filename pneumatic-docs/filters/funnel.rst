
Funnel
------

The funnel is a simple filter that combines its inputs into a single output, like a reverse copy filter. The funnel accepts any number of inputs and a single output. No schema is provided. Consider the following example::

	<pipe id="input1" />
	<pipe id="input2" />
	<pipe id="output" />
	<funnel id="funnel" name="Funnel">
		<input ref="input1" />
		<input ref="input2" />
		<output ref="output" />
	</funnel>

Each input (e.g, ``input ref="input1"``) is read in turn and the record read from that input written to the output (``ref="output"``). No ordering of the inputs to the output is guaranteed. If one of the inputs is empty, or no record is immediately available, the funnel will move to the next input. Each input is continuously read until it is closed. The funnel may process its records "slowly" if any of the inputs has no record available but is not closed. This is due to the read "timing out". If the input is closed, there is no delay.

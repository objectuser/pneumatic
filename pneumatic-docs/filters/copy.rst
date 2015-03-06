Copy
----

The copy filter has a single input pipe. It may have any number of output pipes. Each record on the input is written to each output::

	<pipe id="copyOutput1" />
	<pipe id="copyOutput2" />
	<copy id="copy" name="Test Copy">
		<input ref="fileReaderOutput" />
		<output ref="copyOutput1" />
		<output ref="copyOutput2" />
	</copy>

In this example, the records from the input (``ref="fileReaderOutput"``) are sent, unaltered, to both outputs (``ref="copyOutput1"``, ``ref="copyOutput2"``).


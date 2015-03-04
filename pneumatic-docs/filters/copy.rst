Copy
----

The copy filter has a single input pipe. It may have any number of output pipes. Each record on the input is written to each output::

	<etl:pipe id="copyOutput1" />
	<etl:pipe id="copyOutput2" />
	<etl:copy id="copy" name="Test Copy">
		<etl:input ref="fileReaderOutput" />
		<etl:output ref="copyOutput1" />
		<etl:output ref="copyOutput2" />
	</etl:copy>

In this example, the records from the input (``ref="fileReaderOutput"``) are sent, unaltered, to both outputs (``ref="copyOutput1"``, ``ref="copyOutput2"``).


File Reader
-----------

The file reader reads records from a file and writes them to its output. Records are read using a CSV format. A schema is required to define the records. Consider the following example::

	<pipe id="fileReaderOutput" />
	<fileReader id="fileReader" name="File Reader">
		<fileResource location="classpath:data/mtb.txt" />
		<output ref="fileReaderOutput" />
		<outputSchema ref="mtbSchema" />
	</fileReader>

The file reader reads from the file given by its location (``location="classpath:data/mtb.txt"``). Alternatively, a job argument may be used to specify the file location::1

	<fileResource locationExpression="#job.file" />

The records read from the file are put on the output pipe (``ref="fileReaderOutput"``). The records are constructed according to the supplied schema (``ref="mtbSchema"``).

The file reader supports the ``rejection`` element for controlling rejected records::

	<pipe id="fileReaderOutput" />
	<pipe id="fileReaderRejectionOutput" />
	<fileReader id="fileReader" name="File Reader">
		<fileResource location="classpath:data/bad-data.txt" />
		<output ref="fileReaderOutput" />
		<outputSchema ref="mtbSchema" />
		<rejection>
			<output ref="fileReaderRejectionOutput" />
		</rejection>
	</fileReader>

By default, rejected records are logged.

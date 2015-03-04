File Reader
-----------

The file reader reads records from a file and writes them to its output. Records are read using a CSV format. A schema is required to define the records. Consider the following example::

	<etl:pipe id="fileReaderOutput" />
	<etl:fileReader id="fileReader" name="File Reader">
		<etl:fileResource location="classpath:data/mtb.txt" />
		<etl:output ref="fileReaderOutput" />
		<etl:outputSchema ref="mtbSchema" />
	</etl:fileReader>

The file reader reads from the file given by its location (``location="classpath:data/mtb.txt"``). Alternatively, a job argument may be used to specify the file location::1

	<etl:fileResource locationExpression="#job.file" />

The records read from the file are put on the output pipe (``ref="fileReaderOutput"``). The records are constructed according to the supplied schema (``ref="mtbSchema"``).

The file reader supports the ``rejection`` element for controlling rejected records::

	<etl:pipe id="fileReaderOutput" />
	<etl:pipe id="fileReaderRejectionOutput" />
	<etl:fileReader id="fileReader" name="File Reader">
		<etl:fileResource location="classpath:data/bad-data.txt" />
		<etl:output ref="fileReaderOutput" />
		<etl:outputSchema ref="mtbSchema" />
		<etl:rejection>
			<etl:output ref="fileReaderRejectionOutput" />
		</etl:rejection>
	</etl:fileReader>

By default, rejected records are logged.

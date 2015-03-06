
File Writer
-----------

The file writer reads records from its input and writes them to a file. The input schema in optional. If supplied, records will be validated against the schema and rejected if they don't conform. Records are written in a CSV format. Consider the following example::
	
	<pipe id="fileReaderOutput" />
	<fileWriter id="fileWriter" name="File Writer">
		<fileResource location="file:output/output1.txt" />
		<input ref="fileReaderOutput" />
		<inputSchema ref="mtbSchema" />
	</fileWriter>

The file location (``location="file:output/output1.txt"``) uses the `Spring resource syntax <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/resources.html>`_. Alternatively, a job parameter may be used to supply the file location::

	<fileResource locationExpression="#job.file" />

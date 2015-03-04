
File Writer
-----------

The file writer reads records from its input and writes them to a file. The input schema in optional. If supplied, records will be validated against the schema and rejected if they don't conform. Records are written in a CSV format. Consider the following example::
	
	<etl:pipe id="fileReaderOutput" />
	<etl:fileWriter id="fileWriter" name="File Writer">
		<etl:fileResource location="file:output/output1.txt" />
		<etl:input ref="fileReaderOutput" />
		<etl:inputSchema ref="mtbSchema" />
	</etl:fileWriter>

The file location (``location="file:output/output1.txt"``) uses the `Spring resource syntax <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/resources.html>`_. Alternatively, a job parameter may be used to supply the file location::

	<etl:fileResource locationExpression="#job.file" />

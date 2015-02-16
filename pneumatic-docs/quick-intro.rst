***********
Quick Intro
***********

Pneumatic.IO is a fresh approach to ETL and structured IO. It's a development platform, but little to no programming is required. This section provides a preview of Pneumatic. The concepts referenced here will be explained in the rest of this guide, but this section should provide a flavor of what it's all about.

Pneumatic is declarative, using an XML markup  based on Spring's support for extensible markup. I hope in the future there will be a GUI for creating Pneumatic jobs. But for now, the XML provides a proof of concept that still makes creating ETL jobs really easy. There might be, however, a conceptual hurdle of understanding how Spring declares beans.

As a quick example, reading from a file and writing to a database might look like this (omitting some boilerplate XML)::

	<jdbc:embedded-database id="dataSource" type="HSQL">
		<jdbc:script location="classpath:db-schema.sql" />
		<jdbc:script location="classpath:db-test-data.sql" />
	</jdbc:embedded-database>

	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="name" type="string" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

	<etl:pipe id="fileReaderOutput" />
	<etl:fileReader id="fileReader" name="File Reader">
		<etl:fileResource location="classpath:data/mtb.txt" />
		<etl:output ref="fileReaderOutput" />
		<etl:outputSchema ref="mtbSchema" />
	</etl:fileReader>

	<etl:databaseWriter id="databaseWriter" name="Database Writer">
		<etl:input ref="fileReaderOutput" />
		<etl:inputSchema ref="mtbSchema" />
		<etl:dataSource ref="dataSource" />
		<etl:insertInto table-name="mtb" />
	</etl:databaseWriter>

In this guide, configuration elements are indicated by their "IDs" (identifiers). The ID needs to be unique across all the files in your jobs.  An example of an ID is the first declaration (``id="dataSource"``). This refers to a Spring embedded data source. A data source is an object that provides connections to a database like Oracle, SQL Server, MySQL, etc.

Next is a schema declaration (``id="mtbSchema"``)  used to declare the structure of records in the job. A pipe (``id="fileReaderOutput"``) provides a conduit for records, connecting one processing element (called "filters") to another. 

A file reader (``id="fileReader"``) is a filter that reads a file, creating records and sending them to the pipe referenced in its "output". The file reader has an output (``etl:output ref="fileReaderOutput"``), which refers (using the ``ref`` keyword) to the ``fileReaderOutput`` pipe. This guide will often just use the reference portion of an element when describing it (``ref="fileReaderOutput"``).

A database writer (``id="databaseWriter"``) writes records from the pipe referenced in its ``input`` to a table available in the data source (the ``mtb`` table in this case). Don't be confused by the input to the database writer referencing a pipe called ``fileReaderOutput``: pipes connect the output of one filter to the input of another filter. Pipes are often declared near the filter that is writing to them and so are named according to that relationship. A pipe could also be named something like ``fileReaderToDatabaseWriterPipe``. You can use any convention you like.

Declaring these elements provides Pneumatic enough information to read all the records in the file and write them to the database. When there are no more records to process, Pneumatic shuts down. That's it.

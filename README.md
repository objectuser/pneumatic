
# Quick Intro to Pneumatic.IO

[Pneumatic.IO](http://pneumatic.io) is a fresh approach to ETL and structured IO. It's a development platform, but little to no programming is required.

Pneumatic is declarative, using an XML markup based on Spring's support for extensible markup. I hope in the future there will be a GUI for creating Pneumatic jobs. But for now, the XML provides a proof of concept that still makes creating ETL jobs really easy. (There might be, however, a conceptual hurdle of understanding how Spring declares beans.)

As a quick example, reading from a file and writing to a database might look like this (omitting some boilerplate XML):

```xml
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
```

The first declaration (id="dataSource") is a Spring embedded data source. A data source is an object that provides connections to a database like Oracle, SQL Server, MySQL, etc.

Next is a schema declaration (id="mtbSchema")  used to declare the structure of records in the job. A pipe (id="fileReaderOutput") provides a conduit from one processing element (called "filters") to another. A file reader (id="fileReader") reads a file, creating records and sending them to the pipe referenced in its "output".

A database writer (id="databaseWriter") writes records from the pipe referenced in its "input" to a table available in the data source (the "mtb" table in this case).

Declaring these elements provides Pneumatic enough information to read all the records in the file and write them to the database. When there are no more records to process, Pneumatic shuts down. That's it.

Ready for more? Read the full docs [here](http://pneumatic.io/pneumatic/) and get updates [here](http://pneumatic.io/).


Database Writer
---------------

The database writer writes records from its input pipe to a database table. In its simplest form, the table name is supplied and the records are written directly to the table.  Consider the following example::

	<etl:pipe id="fileReaderOutput" />
	<etl:databaseWriter id="databaseWriter" name="Database Writer">
		<etl:input ref="fileReaderOutput" />
		<etl:inputSchema ref="mtbSchema" />
		<etl:dataSource ref="dataSource" />
		<etl:insertInto table-name="mtb" />
	</etl:databaseWriter>

The input is required (``ref="fileReaderOutput"``), as is the input schema (``ref="mtbSchema"``). The data source provides the filter with database connections (``ref="dataSource"``).  Finally, the ``insertInto`` element provides the name of the table into which the records are inserted (``etl:insertInto table-name="mtb"``).

The database writer has a second form for updates.  The following is an example::

	<etl:pipe id="fileReaderOutput" />
	<etl:databaseWriter id="databaseWriter" name="Database Writer">
		<etl:input ref="fileReaderOutput" />
		<etl:inputSchema ref="mtbSchema" />
		<etl:dataSource ref="dataSource" />
		<etl:updateWith>
			<etl:sql>
				update mtb set year = ?, cost = ? where name = ?
			</etl:sql>
			<etl:parameter value="#inputRecord.year" />
			<etl:parameter value="#inputRecord.cost" />
			<etl:parameter value="#inputRecord.name" />
		</etl:updateWith>
	</etl:databaseWriter>

The difference is the ``etl:updateWith`` element replaces the ``etl:insertInto`` element. This element is a bit more complex, requiring a SQL statement be specified. The question marks in the SQL statement are parameter markers. They are supplied by the parameter elements (``etl:parameter``) in the order specified. The value of each parameter may be a constant or an SpEL expression.

The variable ``inputRecord`` may be used to refer to the input record being processed. The columns on the input record may appear after a period or inside brackets.  The first example below refers to the ``year`` column on the input record. The second uses the brackets syntax to refer to the ``name`` column::

	<etl:parameter value="#inputRecord.year" />

	<etl:parameter value="#inputRecord['name']" />

The second syntax is useful for referring to columns that have spaces in the name::

	<etl:parameter value="#inputRecord['Column Name with Spaces']" />

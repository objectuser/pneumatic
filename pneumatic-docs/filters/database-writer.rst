
Database Writer
---------------

The database writer writes records from its input pipe to a database table. In its simplest form, the table name is supplied and the records are written directly to the table.  Consider the following example::

	<pipe id="fileReaderOutput" />
	<databaseWriter id="databaseWriter" name="Database Writer">
		<input ref="fileReaderOutput" />
		<inputSchema ref="mtbSchema" />
		<dataSource ref="dataSource" />
		<insertInto table-name="mtb" />
	</databaseWriter>

The input is required (``ref="fileReaderOutput"``), as is the input schema (``ref="mtbSchema"``). The data source provides the filter with database connections (``ref="dataSource"``).  Finally, the ``insertInto`` element provides the name of the table into which the records are inserted (``insertInto table-name="mtb"``).

The database writer has a second form for updates.  The following is an example::

	<pipe id="fileReaderOutput" />
	<databaseWriter id="databaseWriter" name="Database Writer">
		<input ref="fileReaderOutput" />
		<inputSchema ref="mtbSchema" />
		<dataSource ref="dataSource" />
		<updateWith>
			<sql>
				update mtb set year = ?, cost = ? where name = ?
			</sql>
			<parameter value="#inputRecord.year" />
			<parameter value="#inputRecord.cost" />
			<parameter value="#inputRecord.name" />
		</updateWith>
	</databaseWriter>

The difference is the ``updateWith`` element replaces the ``insertInto`` element. This element is a bit more complex, requiring a SQL statement be specified. The question marks in the SQL statement are parameter markers. They are supplied by the parameter elements (``parameter``) in the order specified. The value of each parameter may be a constant or an SpEL expression.

The variable ``inputRecord`` may be used to refer to the input record being processed. The columns on the input record may appear after a period or inside brackets.  The first example below refers to the ``year`` column on the input record. The second uses the brackets syntax to refer to the ``name`` column::

	<parameter value="#inputRecord.year" />

	<parameter value="#inputRecord['name']" />

The second syntax is useful for referring to columns that have spaces in the name::

	<parameter value="#inputRecord['Column Name with Spaces']" />

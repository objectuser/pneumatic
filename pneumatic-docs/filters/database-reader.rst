
Database Reader
---------------

A database reader reads records from a database identified by a data source. A SQL select statement is used to select the desired records. The records are written to the output pipe.

Consider the following example::

	<pipe id="databaseReaderOutput" />
	<databaseReader id="databaseReader" name="Database Reader">
		<output ref="databaseReaderOutput" />
		<outputSchema ref="sqlSelectSchema" />
		<dataSource ref="dataSource" />
		<select>
			<sql>
				select * from mtb where name = ? and year = ?
			</sql>
			<parameter value="#job.name" />
			<parameter value="#job.model_year" />
		</select>
		<rejection>
			<output ref="databaseReaderRejectionOutput" />
		</rejection>
	</databaseReader>

The output is given by ``ref="databaseReaderOutput"``.

The output schema (``ref="sqlSelectSchema"``) is used to define the output records. Columns from the select statement that match the names of the columns in the output schema are used to provide the values to the output record.

The select element (``select``) specifies the select statement (``sql``) and any parameters. The parameters are optional. Job parameters may be used to provide values to the SQL parameters.

The optional rejection element (``rejection``) defines the behavior for rejecting records. If a record does not comply with the output schema, in this case it is sent to another pipe, ``ref="databaseReaderRejectionOutput"``. The default is to log the record, which can also be specified explicitly::

	<rejection>
		<log level="WARN" name="REJECTION" />
	</rejection>
	
Here, the ``level`` is a `Log4J level <https://logging.apache.org/log4j/2.x/log4j-api/apidocs/org/apache/logging/log4j/Level.html>`_, with all those levels being available.  The ``name`` is the name of the logger, basically categorizing the log statement.

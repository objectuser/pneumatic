Database Lookup
---------------

The database lookup filter enables lookups to a database based on its single input. The input and lookup results may be combined for its single output.

The following are the key elements of the database lookup filter:

#. dataSource - The DataSource that provides connections to the database that provides the lookup data.
#. outputSchema - This is the schema that defines the output records. The filter uses the column definitions on this schema to select values from either input record values or the results of the lookup. The selection is based on the names of the inputs.
#. lookup - The lookup element composes the SQL for the lookup and its parameters.  The parameters use SpEL and may reference the input record (inputRecord) as a variable.

It is important to avoid name collisions to ensure the results are as expected. Use aliasing on the select statement if needed to ensure those columns do not conflict with those on the input schema. Alternatively, use a mapper filter to change the schema of the input before it comes into this filter.

Consider the following example::

	<pipe id="databaseLookupOutput" />
	<databaseLookup id="databaseLookup" name="Database Lookup">
		<input ref="mapperOutput" />
		<inputSchema ref="mapperMtbSchema" />
		<output ref="databaseLookupOutput" />
		<outputSchema ref="lookupSchema" />
		<dataSource ref="dataSource" />
		<lookup>
			<sql>
				select year, cost from mtb where name = ?
			</sql>
			<parameter value="#inputRecord.name" />
		</lookup>
	</databaseLookup>

The data source reference (``id="dataSource"``) is an object of type ``javax.sql.DataSource``.  It might be defined like this::

	<jdbc:embedded-database id="dataSource" type="HSQL">
		<jdbc:script location="classpath:db-schema.sql" />
		<jdbc:script location="classpath:db-test-data.sql" />
	</jdbc:embedded-database>

The lookup element contains the SQL to use for the lookup and any parameters it might have. Parameters are identified by a question mark. The values are specified by the parameter tag and filled in order.

The parameters tag may use expressions. Variables available in the expression include the input record (inputRecord) and the job parameters (job).
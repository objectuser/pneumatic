*******
Filters
*******

Filters are where the action is. Filters perform the processing in a job. There are filters that perform basic IO with files, databases, RESTful end points, etc. There are also filters for joining data sources, transforming it, converting data types, etc.

Examples
========

Filter Reference
================

This section provides a reference for the available filter types.

Aggregator
----------

The aggregator filter has a single input pipe. It has two output pipes: the normal output with the unchanged input record and the aggregator output to which the aggregator's function is applied. The normal output is not required for an aggregator: rather it is a convenience to the developer. The aggregator output is, however, required, as that represents the purpose of the aggregator.

Records are written to the normal output as each record is processed. Records are written to the aggregator output only after all input records have been processed.

Copy
----

The copy filter has a single input pipe. It may have any number of output pipes. Each record on the input is written to each output::

	<etl:pipe id="copyOutput1" />
	<etl:pipe id="copyOutput2" />
	<etl:copy id="copy" name="Test Copy">
		<etl:input ref="fileReaderOutput" />
		<etl:output ref="copyOutput1" />
		<etl:output ref="copyOutput2" />
	</etl:copy>

In this example, the records from the input (``ref="fileReaderOutput"``) are sent, unaltered, to both outputs (``ref="copyOutput1"``, ``ref="copyOutput2"``).

Database Lookup
---------------

The database lookup filter enables lookups to a database based on its single input. The input and lookup results may be combined for its single output.

The following are the key elements of the database lookup filter:

#. dataSource - The DataSource that provides connections to the database that provides the lookup data.
#. outputSchema - This is the schema that defines the output records. The filter uses the column definitions on this schema to select values from either input record values or the results of the lookup. The selection is based on the names of the inputs.
#. lookup - The lookup element composes the SQL for the lookup and its parameters.  The parameters use SpEL and may reference the input record (inputRecord) as a variable.

It is important to avoid name collisions to ensure the results are as expected. Use aliasing on the select statement if needed to ensure those columns do not conflict with those on the input schema. Alternatively, use a mapper filter to change the schema of the input before it comes into this filter.

Consider the following example::

	<etl:pipe id="databaseLookupOutput" />
	<etl:databaseLookup id="databaseLookup" name="Database Lookup">
		<etl:input ref="mapperOutput" />
		<etl:inputSchema ref="mapperMtbSchema" />
		<etl:output ref="databaseLookupOutput" />
		<etl:outputSchema ref="lookupSchema" />
		<etl:dataSource ref="dataSource" />
		<etl:lookup>
			<etl:sql>
				select year, cost from mtb where name = ?
			</etl:sql>
			<etl:parameter value="#inputRecord.name" />
		</etl:lookup>
	</etl:databaseLookup>

The data source reference (``id="dataSource"``) is an object of type ``javax.sql.DataSource``.  It might be defined like this::

	<jdbc:embedded-database id="dataSource" type="HSQL">
		<jdbc:script location="classpath:db-schema.sql" />
		<jdbc:script location="classpath:db-test-data.sql" />
	</jdbc:embedded-database>

The lookup element contains the SQL to use for the lookup and any parameters it might have. Parameters are identified by a question mark. The values are specified by the parameter tag and filled in order.

The parameters tag may use expressions. Variables available in the expression include the input record (inputRecord) and the job parameters (job).

Database Reader
---------------

A database reader reads records from a database identified by a data source. A SQL select statement is used to select the desired records. The records are written to the output pipe.

Consider the following example::

	<etl:pipe id="databaseReaderOutput" />
	<etl:databaseReader id="databaseReader" name="Database Reader">
		<etl:output ref="databaseReaderOutput" />
		<etl:outputSchema ref="sqlSelectSchema" />
		<etl:dataSource ref="dataSource" />
		<etl:select>
			<etl:sql>
				select * from mtb where name = ? and year = ?
			</etl:sql>
			<etl:parameter value="#job.name" />
			<etl:parameter value="#job.model_year" />
		</etl:select>
		<etl:rejection>
			<output ref="databaseReaderRejectionOutput" />
		</etl:rejection>
	</etl:databaseReader>

The output is given by ``ref="databaseReaderOutput"``.

The output schema (``ref="sqlSelectSchema"``) is used to define the output records. Columns from the select statement that match the names of the columns in the output schema are used to provide the values to the output record.

The select element (``etl:select``) specifies the select statement (``etl:sql``) and any parameters. The parameters are optional. Job parameters may be used to provide values to the SQL parameters.

The optional rejection element (``etl:rejection``) defines the behavior for rejecting records. If a record does not comply with the output schema, in this case it is sent to another pipe, ``ref="databaseReaderRejectionOutput"``. The default is to log the record, which can also be specified explicitly::

	<etl:rejection>
		<log level="WARN" name="REJECTION" />
	</etl:rejection>
	
Here, the ``level`` is a `Log4J level <https://logging.apache.org/log4j/2.x/log4j-api/apidocs/org/apache/logging/log4j/Level.html>`_, with all those levels being available.  The ``name`` is the name of the logger, basically categorizing the log statement.
 

Database Writer
---------------

File Reader
-----------


File Writer
-----------


Funnel
------

Join
----

Mapper
------

RESTful Consumer
----------------

RESTful Input
-------------

RESTful Lookup
--------------

Sort
----

Transformer
-----------

The transformer filter may be the most powerful filter, but also the most complex. Also, due to the use of an expression language for most operations, the transformer is generally slower than other stages. The transformer is like an enhanced copy filter: it can have only one input, but any number of outputs. The outputs are controlled through conditions and expressions.

The transformer uses the `Spring SpEL <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html>`_ expression language for expressions. SpEL can be compiled, so it's not a huge performance hit, but it is there.

Consider the following transformer example::

	<etl:pipe id="transformerOutput" />
	<etl:pipe id="transformerCountOutput" />
	<etl:transformer id="transformer" name="Transformer">
		<etl:input ref="fileReaderOutput" />
		<etl:variables>
			<etl:variable name="totalCount">0</etl:variable>
		</etl:variables>
		<etl:expressions>
			<etl:expression>#totalCount = #totalCount + 1</etl:expression>
		</etl:expressions>
		<etl:config outputName="transformerOutput" recordName="outputRecord">
			<etl:output ref="transformerOutput" />
			<etl:schema ref="inputSchema" />
			<etl:expression>#outputRecord.Name = #inputRecord.Name + #inputRecord.Name</etl:expression>
			<etl:expression>#outputRecord.Count = #inputRecord.Count + #inputRecord.Count</etl:expression>
			<etl:expression>#outputRecord.Price = #inputRecord.Price + #inputRecord.Price</etl:expression>
		</etl:config>
		<etl:config outputName="transformerCountOutput" recordName="countRecord">
			<etl:output ref="transformerCountOutput" />
			<etl:schema ref="countSchema" />
			<etl:outputCondition>#input.complete</etl:outputCondition>
			<etl:expression>#countRecord.Count = #totalCount</etl:expression>
		</etl:config>
	</etl:transformer>

The first two elements are two pipes (``id="transformerOutput"`` and ``id="transformerCountOutput"``) that will be outputs from the transformer.

Next, is the transformer is declared (``id="transformer"``).

The first element of the transformer is its single input that, from its name, appears to have come from reading a file. The declaration is not shown above.::

<etl:input ref="fileReaderOutput" />

Next is a set of "output configurations" (``etl:outputConfigurations``) which define the outputs of the transformer. Each configuration (``etl:config``) may have a schema, 

XML File Reader
------------------




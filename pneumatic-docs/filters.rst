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

File Reader
-----------

The file reader reads records from a file and writes them to its output. Records are read using a CSV format. A schema is required to define the records. Consider the following example::

	<etl:pipe id="fileReaderOutput" />
	<etl:fileReader id="fileReader" name="File Reader">
		<etl:fileResource location="classpath:data/mtb.txt" />
		<etl:output ref="fileReaderOutput" />
		<etl:outputSchema ref="mtbSchema" />
	</etl:fileReader>

The file reader reads from the file given by its location (``location="classpath:data/mtb.txt"``). Alternatively, a job argument may be used to specify the file location::

	<etl:fileResource locationExpression="#job.file" />

The records read from the file are put on the output pipe (``ref="fileReaderOutput"``). The records are constructed according to the supplied schema (``ref="mtbSchema"``).

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

Funnel
------

The funnel is a simple filter that combines its inputs into a single output, like a reverse copy filter. The funnel accepts any number of inputs and a single output. No schema is provided. Consider the following example::

	<etl:pipe id="input1" />
	<etl:pipe id="input2" />
	<etl:pipe id="output" />
	<etl:funnel id="funnel" name="Funnel">
		<etl:input ref="input1" />
		<etl:input ref="input2" />
		<etl:output ref="output" />
	</etl:funnel>

Each input (e.g, ``etl:input ref="input1"``) is read in turn and the record read from that input written to the output (``ref="output"``). No ordering of the inputs to the output is guaranteed. If one of the inputs is empty, or no record is immediately available, the funnel will move to the next input. Each input is continuously read until it is closed. The funnel may process its records "slowly" if any of the inputs has no record available but is not closed. This is due to the read "timing out". If the input is closed, there is no delay.

Join
----

The join filter joins two inputs based on a comparison of the records in the inputs. The inputs must be sorted according to the join (``etl:comparator``) criteria prior to use in the join filter. This means either using a sort filter or otherwise ensuring the inputs are sorted. The sort criteria must be the same as the join criteria. Consider the following example::

	<etl:pipe id="innerJoinOutput" />
	<etl:join id="innerJoinFilter" name="Inner Join">
		<etl:leftInput ref="joinInput1" />
		<etl:rightInput ref="joinInput2" />
		<etl:output ref="innerJoinOutput" />
		<etl:outputSchema ref="inputSchema" />
		<etl:comparator>
			<etl:column name="Name" type="string" />
		</etl:comparator>
	</etl:join>

As stated, the two inputs (``etl:leftInput ref="joinInput1"`` and ``etl:rightInput ref="joinInput2"``) must be sorted using the same ordering as the ``comparator``. In this case, the inputs must be sorted by the ``Name`` column of the inputs.

The comparator is used to join the records based on the column provided. The default comparator only accepts a single column for the comparison. A single output record is created from the joined records. The output schema (``ref="inputSchema"``) is used to select the columns from the input records. If there is no record in the ``rightInput`` that matches the current record in the ``leftInput`` no output record is written.

The join filter has a second form::

	<etl:pipe id="outerJoinOutput" />
	<etl:join id="outerJoinFilter" name="Outer Join">
		<etl:leftInput ref="joinInput1" />
		<etl:rightInput ref="joinInput2" />
		<etl:output ref="outerJoinOutput" />
		<etl:outputSchema ref="inputSchema" />
		<etl:comparator>
			<etl:column name="Name" type="string" />
		</etl:comparator>
		<etl:leftOuterJoin />
	</etl:join>

The difference is the ``etl:leftOuterJoin`` element. With this element, if there is no record in the ``rightInput`` that matches a record in the ``leftInput``, the output record is still created and sent to the output.

The join filter may be extended for other forms of comparison. Creating your own comparator may require some pramming and uses the Spring bean syntax. Consider the following example::

	<etl:join id="innerJoinWithCustomComparator" name="Inner Join with Custom Comparator">
		<etl:leftInput ref="input1" />
		<etl:rightInput ref="input2" />
		<etl:output ref="output" />
		<etl:outputSchema ref="inputSchema" />
		<etl:comparator>
			<bean class="com.surgingsystems.etl.filter.JoinFilterTest.TwoColumnComparator">
				<property name="columns">
					<list>
						<etl:column name="Name" type="string" />
						<etl:column name="Count" type="integer" />
					</list>
				</property>
			</bean>
		</etl:comparator>
	</etl:join>
	
Here, the comparator is provided by a Spring Bean with the given class::

	<bean class="com.surgingsystems.etl.filter.JoinFilterTest.TwoColumnComparator">

The rest of the bean definition is specific to the class itsef, but note that Pneumatic elements may be used in defining the bean::

	<list>
		<etl:column name="Name" type="string" />
		<etl:column name="Count" type="integer" />
	</list>

The ``etl:column`` elements are the same elements used to define schemas in Pneumatic.

Mapper
------

The mapper translates records that conform to an input schema to records that conform to an output schema. In its simplest form, the output schema provides enough information to perform the mapping. Consider the following example::

	<etl:schema id="giantBikesSchema" name="Giant Bikes Schema">
		<etl:column name="name" type="string" />
		<etl:column name="bike_number" type="integer" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="name" type="string" />
		<etl:column name="bike_number" type="string" />
		<etl:column name="year" type="integer" />
	</etl:schema>

	<etl:pipe id="input" />
	<etl:pipe id="mapperOutput" />
	<etl:mapper id="simpleMapper" name="Simple Mapper">
		<etl:input ref="input" />
		<etl:inputSchema ref="giantBikesSchema" />
		<etl:output ref="mapperOutput" />
		<etl:outputSchema ref="mtbSchema" />
	</etl:mapper>
	
The mapper filter (``mapper id="simpleMapper"``) uses the output schema (``ref="mtbSchema"``) to choose columns from the input records. The columns are chosen by name. In this example, the input has four columns available and three are selected for output: ``name``, ``bike_number`` and ``year``.

Also note that the type of the ``bike_number`` column is changed from integer to string. The mapper is able to make reasonable type conversions automatically: integer and decimal to string and integer to decimal.

More complicated translations are also supported as shown in the following example::

	<etl:schema id="giantBikesSchema" name="Giant Bikes Schema">
		<etl:column name="name" type="string" />
		<etl:column name="bike_number" type="integer" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="model_name" type="string" />
		<etl:column name="model_number" type="string" />
		<etl:column name="model_year" type="integer" />
		<etl:column name="unit_price" type="decimal" />
	</etl:schema>

	<etl:pipe id="mapperOutput" />
	<etl:mapper id="mapper" name="Mapper">
		<etl:input ref="input" />
		<etl:inputSchema ref="giantBikesSchema" />
		<etl:output ref="mapperOutput" />
		<etl:outputSchema ref="mtbSchema" />
		<etl:mappings>
			<etl:mapping>
				<etl:from>
					<etl:column name="name" type="string" />
				</etl:from>
				<etl:to>
					<etl:column name="model_name" type="string" />
				</etl:to>
			</etl:mapping>
			<etl:mapping>
				<etl:from>
					<etl:column name="bike_number" type="integer" />
				</etl:from>
				<etl:to>
					<etl:column name="model_number" type="string" />
				</etl:to>
			</etl:mapping>
			<etl:mapping>
				<etl:from>
					<etl:column name="year" type="integer" />
				</etl:from>
				<etl:to>
					<etl:column name="model_year" type="integer" />
				</etl:to>
			</etl:mapping>
			<etl:mapping>
				<etl:from>
					<etl:column name="cost" type="decimal" />
				</etl:from>
				<etl:to>
					<etl:column name="unit_price" type="decimal" />
				</etl:to>
			</etl:mapping>
		</etl:mappings>
	</etl:mapper>

The ``etl:mappings`` element allows for explicit mappings between input and output columns using the ``etl:from`` and ``etl:to`` elements. With this form, the column names do not need to match and type conversion is still supported.

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




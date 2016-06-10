.. _database-lookup:

Database Lookup
===============

Description
-----------

The database lookup filter enables lookups to a database based on its single input. The input and lookup results may be combined for its single output.

The following are the key elements of the database lookup filter:

#. dataSource - The DataSource that provides connections to the database that provides the lookup data.
#. outputSchema - This is the schema that defines the output records. The filter uses the column definitions on this schema to select values from either input record values or the results of the lookup. The selection is based on the names of the inputs.
#. sql and parameters - The lookup is performed with the SQL and the set of parameters. The parameters use SpEL and may reference the input record (``inputRecord``) as a variable.

It is important to avoid name collisions to ensure the results are as expected. Use aliasing on the select statement if needed to ensure those columns do not conflict with those on the input schema. Alternatively, use a mapper filter to change the schema of the input before it comes into this filter.

Consider the following example::

  input:
  output:
  databaseLookup:
    name: Database Lookup
    input: input
    inputSchema: mtbSchema
    output: output
    outputSchema: sqlSelectSchema
    dataSource: dataSource
    sql: select cost from mtb where name = ? and year = ?
    parameters:
      - "#inputRecord.name"
      - "#inputRecord.model_year"

Here's the equivalent XML::

  <pipe id="input" />
  <pipe id="output" />
  <databaseLookup id="databaseLookup" name="Database Lookup">
    <input ref="input" />
    <inputSchema ref="mapperMtbSchema" />
    <output ref="output" />
    <outputSchema ref="lookupSchema" />
    <dataSource ref="dataSource" />
    <lookup>
      <sql>
        select year, cost from mtb where name = ? and year = ?
      </sql>
      <parameter value="#inputRecord.name" />
      <parameter value="#inputRecord.model_year" />
    </lookup>
  </databaseLookup>

The data source reference (``dataSource``) is an object of type ``javax.sql.DataSource``. It might be defined like this in Spring::

  <jdbc:embedded-database id="dataSource" type="HSQL">
    <jdbc:script location="classpath:db-schema.sql" />
    <jdbc:script location="classpath:db-test-data.sql" />
  </jdbc:embedded-database>

The ``sql`` and ``parameters`` contain the SQL to use for the lookup and any parameters it might have. Parameters are identified by a question mark in the SQL statement. The values are specified by the ``parameters`` list (in YAML, or successive ``parameter`` tags in XML) and filled in order.

The parameters tag may use expressions. Variables available in the expression include the input record (``inputRecord``) and the job parameters (``job``).

Specification
-------------

**databaseLookup**
  Use a database to lookup data to add to output records

Markup:

* YAML: ``!databaseLookup``
* XML: ``<databaseLookup />``

Properties:

``name``
  the name of the filter, used in logging

``input``
  the pipe providing records into the filter 

``inputSchema`` : optional
  the schema of the records coming into the filter

  * if supplied, input records are validated against the schema and rejected if the validation fails

``output``
  the pipe to which output records are written

``outputSchema``
  the schema of the records written to ``output``
  
  The output schema is used to select column values consisting of the set of values from the input and the lookup (SQL result). The values are selected by name. If a value exists on both the input and the lookup, the value selected is undefined.

``dataSource``
  the source of connections to the target database

``sql``
  the SQL used to perform the lookup

``parameters`` : optional : XML <parameter />
  the parameters for the SQL statement

``rejection`` : optional
  the strategy for rejected records

  ``output``
    supply a pipe for the rejected records
    
  ``log`` : default
    log the rejected records
    
    ``name``
      the name of the logger (sometimes called the "category")
        
    ``level``
      the logging level; one of: ``OFF``, ``FATAL``, ``ERROR``, ``WARN``, ``INFO``, ``DEBUG``, ``TRACE``, ``ALL`` (see the `Log4J2 reference <https://logging.apache.org/log4j/2.0/log4j-api/apidocs/index.html>`_ for additional information)

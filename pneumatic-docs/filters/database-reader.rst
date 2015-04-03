.. _database-reader:

Database Reader
===============

Description
-----------

A database reader reads records from a database identified by a data source. A SQL select statement is used to select the desired records. The records are written to the output pipe.

Consider the following example::

  output: !pipe
  rejectionOutput: !pipe
  databaseReader: !databaseReader
    name: Database Reader
    output: ->output
    outputSchema: ->outputSchema
    dataSource: ->dataSource
    sql: select * from mtb where name = ? and year = ?
    parameters:
      - #job.name
      - #job.model_year
    rejection:
      output: ->rejectionOutput

Here is the same example in XML::

  <pipe id="output" />
  <pipe id="rejectionOutput" />
  <databaseReader id="databaseReader" name="Database Reader">
    <output ref="output" />
    <outputSchema ref="outputSchema" />
    <dataSource ref="dataSource" />
    <select>
      <sql>
        select * from mtb where name = ? and year = ?
      </sql>
      <parameter value="#job.name" />
      <parameter value="#job.model_year" />
    </select>
    <rejection>
      <output ref="rejectionOutput" />
    </rejection>
  </databaseReader>

The output is given by ``ref="databaseReaderOutput"``.

The output schema (``outputSchema"``) is used to define the output records. Columns from the select statement that match the names of the columns in the output schema are used to provide the values to the output record.

The ``sql`` element SQL select statement, with the ``parameters`` applied in order. The parameters are optional. Job parameters may be used to provide values to the SQL parameters.

The optional rejection element (``rejection``) defines the behavior for rejecting records. If a record does not comply with the output schema, in this case it is sent to another pipe, ``rejectionOutput``. The default is to log the record, which can also be specified explicitly::

  rejection:
    log:
      name: REJECTION
      level: WARN

Or in XML::

  <rejection>
    <log level="WARN" name="REJECTION" />
  </rejection>
  
Here, the ``level`` is a `Log4J level <https://logging.apache.org/log4j/2.x/log4j-api/apidocs/org/apache/logging/log4j/Level.html>`_, with all those levels being available.  The ``name`` is the name of the logger, basically categorizing the log statement.

Specification
-------------

**databaseReader**
  Read records from a database

Markup:

* YAML: ``!databaseReader``
* XML: ``<databaseReader />``

Properties:

``name``
  the name of the filter, used in logging

``output``
  the pipe to which output records are written

``outputSchema``
  the schema of the records written to ``output``
  
  The output schema is used to select column values consisting of the set of values from the select statement (SQL result). The values are selected by name.

``dataSource``
  the source of connections to the target database

``sql``
  the aggregation function of the filter

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

.. _database-writer:

Database Writer
===============

Description
-----------

The database writer writes records from its input pipe to a database table. In its simplest form, the table name is supplied and the records are written directly to the table.  Consider the following example::

  input: !pipe
  databaseWriter: !databaseWriter
    name: Database Writer
    input: input
    inputSchema: mtbSchema
    dataSource: dataSource
    insertInto: mtb

The equivalent in XML is::

  <pipe id="input" />
  <databaseWriter id="databaseWriter" name="Database Writer">
    <input ref="input" />
    <inputSchema ref="mtbSchema" />
    <dataSource ref="dataSource" />
    <insertInto table-name="mtb" />
  </databaseWriter>

The ``input`` is required, as is ``inputSchema``. The ``dataSource`` provides the filter with database connections.  Finally, the ``insertInto`` element provides the name of the table into which the records are inserted (``insertInto: mtb``).

The database writer has a second form for updates.  The following is an example::

  input: !pipe
  databaseWriter: !databaseWriter
    name: Database Writer
    input: input
    inputSchema: mtbSchema
    dataSource: dataSource
    updateWith:
      sql: update mtb set year = ?, cost = ? where name = ?
      parameters:
        - "#inputRecord.year"
        - "#inputRecord.price"
        - "#inputRecord.name"

The equivalent in XML is::

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

The difference is the ``updateWith`` element replaces the ``insertInto`` element. This element is a bit more complex, requiring a SQL statement be specified. The question marks in the SQL statement are parameter markers. They are supplied by the list of ``parameters`` in the order specified.

The value of each parameter may be a constant or an SpEL expression. The variable ``inputRecord`` may be used to refer to the input record being processed. The columns on the input record may appear after a period or inside brackets. The first example below refers to the ``year`` column on the input record. The second uses the brackets syntax to refer to the ``name`` column::

  .. code:: yaml
  #inputRecord.year

  #inputRecord['name']

The second syntax is useful for referring to columns that have spaces in the name::

  #inputRecord['Column Name with Spaces']

Specification
-------------

**databaseWriter**
  Write input records to a database

Markup:

* YAML: ``!databaseWriter``
* XML: ``<databaseWriter />``

Properties:

``name``
  the name of the filter, used in logging

``input``
  the pipe from which input records are read

``inputSchema``
  the schema of the records read from ``input``

``dataSource``
  the source of connections to the target database

``insertInto`` : optional
  provides the table name into which records are inserted
  
``updateWith`` : optional
  specifies an update strategy

  ``sql``
    the SQL used to perform the update

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
  
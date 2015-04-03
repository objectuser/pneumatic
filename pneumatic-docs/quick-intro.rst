***********
Quick Intro
***********

Pneumatic.IO is a fresh approach to ETL and structured IO. It's a development platform, but little to no programming is required.

Pneumatic is declarative, using either a custom YAML markup, or XML markup based on Spring's support for extensible markup. I hope in the future there will be a GUI for creating Pneumatic jobs. But for now, the texutal forms provide a proof of concept that still makes creating ETL jobs really easy.

As a quick example, here's how you might read from a file and write its contents to a database in Pneumatic.

First, here is a Spring data source definition::

  <jdbc:embedded-database id="dataSource" type="HSQL">
    <jdbc:script location="classpath:db-schema.sql" />
    <jdbc:script location="classpath:db-test-data.sql" />
  </jdbc:embedded-database>

Next, here are the YAML-based Pneumatic declarations::

  # Declare a schema that defines our records
  mtbSchema: !schema
    name: Input Schema
    columns:
      - name: name
        type: string
      - name: year
        type: integer
      - name: cost
        type: decimal
  
  # Declare a pipe to join the file reader and database writer
  fileReaderOutput: !pipe
  # Declare a file reader to read from mtb.txt
  mtbFileReader: !fileReader
    name: File Reader
    fileResource: data/mtb.txt
    output: ->fileReaderOutput
    outputSchema: ->inputSchema
  
  # Declare a database writer to read from the pipe and write to the mtb table
  mtbDatabaseWriter: !databaseWriter
    name: Database Writer
    input: ->fileReaderOutput
    inputSchema: ->mtbSchema
    dataSource: ->dataSource # Reference the data source declared in Spring XML
    insertInto: mtb

The first declaration (``id="dataSource"``) is a Spring embedded data source. A data source is an object that provides connections to a database like Oracle, SQL Server, MySQL, etc.

Next is a schema declaration (``mtbSchema``)  used to declare the structure of records in the job. A pipe (``fileReaderOutput``) provides a conduit from one processing element (called "filters") to another. A file reader (``mtbFileReader``) reads a file, creating records and sending them to the pipe referenced in its ``output``.

A database writer (`mtbDatabaseWriter`) writes records from the pipe referenced in its ``input`` to a table available in the data source: the ``mtb`` table referenced in the ``insertInto`` property in this case.

Declaring these elements provides Pneumatic enough information to read all the records in the file and write them to the database. When there are no more records to process, Pneumatic shuts down. That's it.

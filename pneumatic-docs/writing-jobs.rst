************
Writing Jobs
************

An Assembly Line
================

Pneumatic.IO takes a simple and powerful approach to the creation of jobs. A Pneumatic "job" is an assembly of "filters" that provide processing features, connected together with "pipes" that allow data to move from one filter to the next.

You might think of a Pneumatic job as an assembly line. An assembly line consists of some arbitrary number of "stations", that are major steps in the assembly process. Each station may use some materials as part of its process. Once a station is done with its part of the process, it sends the result down the assembly line to the next station.

In Pneumatic, the assembly line stations are "filters". Filters provide processing on input data, like the materials that are processed at an assembly line station. Once a filter is done with its work, it sends the result to the next filter, or maybe to a file or database if the result is complete (fully assembled).

Filters work on "records" that are similar to records in a database. A filter works on one record at a time. When a filter is done working on a record, it may send the same record to its output. Or, more often, a modified form of the record that is a result of the filter's processing.

Some filters have multiple inputs and produce outputs based on some combination of the inputs. Some filters have multiple outputs, using its input to send records to each output. Some filters read from files or databases, or write to files or databases. These data sources are often the original source of the records processed by a Pneumatic job. Writing to data sources is often the final output of the job.

A Stepwise Approach
===================

Building on the assembly line analogy, many problems are best solved by breaking them down into small steps. Small steps are usually easier to understand. Also, if something is wrong in one (small) step, it can often be more easily understood and fixed.

Pneumatic.IO comes with a variety of filters that do one and only one thing. Jobs can be built from these simple filters joined together to form a complete assembly line for your process.

Pneumatic also comes with more powerful, feature rich and complicated filters, like the :ref:`Transformer <transformer>`. The Transformer can do just about anything the other processing filters can do, but it takes more work. When you need the power of the Transformer, it's great to have it. But try doing things without it so your jobs are simpler and easier to understand.


A Simple Job
============

As an example of a very simple job, consider the process of reading from one file and writing to another file. Pneumatic include a filter for reading from a file. It also includes a filter for writing to a file. All we need to do is configure these filters and connect them with a pipe.

Pneumatic.IO has both an XML and YAML syntax. The YAML syntax is far less verbose and human readable, so this document will focus on the YAML syntax. If you're not familiar with YAML, the `Wikipedia topic has a quick intro <https://en.wikipedia.org/wiki/YAML>`_.

Here's the job:

.. code-block:: yaml

  ---
  # Define the structure of the data in the file
  fileSchema:
    name: Input Schema
    columns:
      - name: Name
        type: string
      - name: Count
        type: integer
      - name: Price
        type: decimal

  # A pipe to connect the filters
  fileReaderOutput:

  # Read the file
  fileReader:
    name: File Reader
    fileResource: file:src/test/resources/data/input1.txt
    output: fileReaderOutput
    outputSchema: fileSchema

  # Write the file
  fileWriter:
    name: File Writer 1
    fileResource: file:output/output1.txt
    input: fileReaderOutput
    inputSchema: fileSchema

The first thing is something we didn't talk about: the "schema". A schema here means the same thing as in relational databases: it defines the structure of the data. The schema's identifier is ``fileSchema`` and it has a human friendly name of "Input Schema". The schema has three columns.

Next we define the pipe that connects the two filters: ``fileReaderOutput``.

The ``fileReader`` is the job's first filter. It has the following configuration:

``name``
  the name of the filter
  
``fileResource``
  the location of the file to read

``output``
  the pipe that gets the records read from the file 

``outputSchema``
  the schema of the records sent to the ``output``

Because the ``fileSchema`` has three columns, the File Reader filter will read the first three columns of each line in the file, in order. Also, the file reader expects comma-separated values (CSV) in the record by default.

Here's an example file (``input1.txt`` provided in the ``pneumatic-samples`` project)::

  one,1,100.0
  two,2,200.0
  three,3,300.0
  five,5,500.0

The ``fileWriter`` reads the output from ``fileReader`` as its input because it's hooked up to the same pipe. It uses the same schema and writes the record to the ``fileResource`` property.

More About Schemas
------------------

Schemas have a dual purpose::

1. Provide information to filters on how to interpret data
1. Allow a filter to validate data according to the schema




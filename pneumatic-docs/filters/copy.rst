.. _copy:

Copy
====

Description
-----------

The copy filter has a single input pipe. It may have any number of output pipes. Each record on the input is written to each output::

  output1: !pipe
  output2: !pipe
  duplicateCopy:
    name: Duplicate
    input: fileReaderOutput
    outputs:
      - output1
      - output2

Here's the same example in XML, which has a slightly different form::

  <pipe id="output1" />
  <pipe id="output2" />
  <copy id="duplicate" name="Duplicate">
    <input ref="fileReaderOutput" />
    <output ref="output1" />
    <output ref="output2" />
  </copy>

In this example, the records from the input (``fileReaderOutput``) are sent, unaltered, to both outputs (``output1``, ``output2``).


Specification
-------------

**copy**
  Copy records on the input to each output

Markup:

* YAML: ``!copy``
* XML: ``<copy />``

Properties:

``name``
  the name of the filter, used in logging

``input``
  the pipe providing records into the filter 

``outputs``
  a list of output pipes to which records are copied

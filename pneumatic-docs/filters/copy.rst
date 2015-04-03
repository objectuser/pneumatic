.. _copy:

Copy
====

Description
-----------

The copy filter has a single input pipe. It may have any number of output pipes. Each record on the input is written to each output::

  output1: !pipe
  output2: !pipe
  duplicate: !copy
    name: Duplicate
    input: ->fileReaderOutput
    outputs:
      - ->output1
      - ->output2

Here's the same example in XML, which has a slightly different form::

  <pipe id="copyOutput1" />
  <pipe id="copyOutput2" />
  <copy id="duplicate" name="Duplicate">
    <input ref="fileReaderOutput" />
    <output ref="copyOutput1" />
    <output ref="copyOutput2" />
  </copy>

In this example, the records from the input (``ref="fileReaderOutput"``) are sent, unaltered, to both outputs (``ref="copyOutput1"``, ``ref="copyOutput2"``).


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

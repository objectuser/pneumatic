.. _splitter:

Splitter
--------

The splitter provides a way to split a stream of data based on some values in the data itself. This is known as "`partitioning <http://en.wikipedia.org/wiki/Partition_%28database%29>`_". The splitter is intended to provide support for partitioning data or splitting it for any other purpose.

The splitter is like an enhanced :ref:`copy` or a simplified :ref:`transformer`. Like those, it accepts a single input and supports any number of outputs. Like the transformer it uses the `Spring SpEL <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html>`_ expression language for expressions to determine the partitioning scheme.

Consider the following transformer example::

  <pipe id="input" />
  <pipe id="output1" />
  <pipe id="output2" />
  <splitter id="splitter" name="Splitter">
    <input ref="input" />
    <outputConfiguration name="aThroughM">
      <output ref="output1" />
      <outputCondition>#inputRecord.Name.substring(0, 1) &lt; 'N'</outputCondition>
    </outputConfiguration>
    <outputConfiguration name="nThroughZ">
      <output ref="output2" />
      <outputCondition>#inputRecord.Name.substring(0, 1) &gt; 'M'</outputCondition>
    </outputConfiguration>
  </splitter>

The first elements are pipes: one input (``id="input"``) and two outputs (``id="output1"``, ``id="output2"``) that will be outputs from the splitter.

Next, the splitter is declared (``id="splitter"``).

The first element of the splitter is its single input::

  <input ref="input" />

Next in the example is a set of "output configurations". These define the outputs of the splitter. Each configuration (``outputConfiguration``) has a name (e.g., ``name="aThroughM"``) which supports logging and debugging but plays no other role in the functioning of the splitter.

The output element (e.g., ``output ref="output1"``) of the output configuration defines the output pipe to which a configuration writes records.

The output condition (``outputCondition``) element of an output configuration determines if an input record is written to the output of that configuration. If the value of the expression is true, the record is written to the output. Otherwise, no record is written. In this example, the ``output1`` and ``output2`` pipes get records in opposite conditions: if the ``Name`` column begins with a letter that is less than 'N', ``output1`` will get a record; but if it is greater than 'M', ``output2`` will get a record.

(Note that this one of the cases in which XML is not the best format for expressing filter configurations: we must use the literal ``&lt;`` to express ``<`` and ``&gt;`` to express ``>``. Consult a `reference <http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references>`_ when creating expressions that cause issues with XML.)

Note that the splitter, like the copy filter, does not use any schemas: input records are writter unmodified to the outputs. 

In theory, the splitter could be used to split an input stream and feed those into a series of :ref:`restful-writer` filters that send data over a network to different machines that operate on that subset of data. In this way a Pnematic.IO job might be scaled to support large workloads.
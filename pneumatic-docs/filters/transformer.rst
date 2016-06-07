.. _transformer:

Transformer
-----------

The transformer filter may be the most powerful filter, but also the most complex. The transformer is like an enhanced copy filter: it supports a single input, but any number of outputs. The outputs may be controlled through conditions and expressions.

The transformer uses the `Spring SpEL <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html>`_ expression language for expressions. The SpEL expressions are compiled, so they should not incur a large performance penalty.

Consider the following transformer example::

  input:
  transformerOutput:
  transformerOrderOutput:
  transformerOrderCountOutput:

  validatingTransformer
    name: Validating Transformer
    input: input
    variables:
      lastName: "''"
      orderingProblem: false
      orderingProblemCount: 0
    expressions:
      - "#orderingProblem = #lastName > #inputRecord.Name"
      - "#orderingProblemCount = #orderingProblemCount + (#orderingProblem ? 1 : 0)"
      - "#lastName = #inputRecord.Name"
    outputConfigurations:
      - name: transformerOutput
        recordName: outputRecord
        output: transformerOutput
        outputSchema: inputSchema
        outputCondition: "!#orderingProblem"
        expressions:
          - "#outputRecord.Name = #inputRecord.Name"
          - "#outputRecord.Count = #inputRecord.Count"
          - "#outputRecord.Price = #inputRecord.Price"
      - name: transformerOrderOutput
        recordName: invalidRecord
        output: transformerOrderOutput
        outputSchema: inputSchema
        outputCondition: "#orderingProblem"
        expressions:
          - "#invalidRecord.Name = #inputRecord.Name"
          - "#invalidRecord.Count = #inputRecord.Count"
          - "#invalidRecord.Price = #inputRecord.Price"
      - name: transformerOrderCountOutput
        recordName: invalidCountRecord
        output: transformerOrderCountOutput
        outputSchema: countSchema
        outputCondition: "#input.complete"
        expressions:
          - "#invalidCountRecord.Count = #orderingProblemCount"

The first elements are pipes: one input (``input``) and some outputs (``transformerOutput``, ``transformerOrderOutput``, ``transformerOrderCountOutput``) that will be outputs from the transformer.

Next, the transformer is declared (``validatingTransformer``).

After its name, the first element of the transformer is its single input. This sets the input of the transformer to the pipe named "input"::

  input: input

Next is a set of variables. The variables may be initialized to any constant value. The initialization of the variables occurs before any records have been processed. The values of previously declared variables or input records are not available for initialization. The variables may be used in the expressions section and in the output configuration for both output conditions and expressions.

The variables are followed by a set of expressions. The expressions are executed for each input record processed by the filter. The following expression sets the ``orderingProblem`` boolean value to true or false, depending on if the value of ``lastName`` is alpha-numerically greater than the value of the ``Name`` column on the current input record::

  "#orderingProblem = #lastName > #inputRecord.Name"

This means that if, for example, ``lastName`` is "Mojo" and the value of ``inputRecord.Name`` is "Bronson", ``lastName`` will be set to "true" because "M" comes after "B" in the alphabet.

There are two forms for accessing a column on a record. One is using the "dot" notation as above: ``inputRecord.Name``. The name of the record is before the dot and the column name is after the dot. An alternative is to use brackets: "[]". This is especially useful if the column name contains spaces. In the following example, the column name is "Column Name with Spaces"::

  "#inputRecord['Column Name with Spaces'] = 'A useful value'"

Next in the example is a list of "output configurations". These define the outputs of the transformer. Each configuration has a number of attributes. First is the ``name``, which is used in logging. Second is the ``recordName``, which defines a variable for the record that will be output for that configuration. Next is the ``output`` which sets the pipe for the output configuration. In this example the ``recordName`` is used in each configuration::

  "#outputRecord.Count = #inputRecord.Count"
  
This expression sets the value of the ``Count`` column on the ``outputRecord`` to the value of the ``Count`` column on the ``inputRecord``. The ``inputRecord`` is an automatic variable available to all expressions, capturing the current input record of the filter.

The output condition (``outputCondition``) element controls the output of a configuration. If the value of the expression is true, the record is written to the output. Otherwise, no record is written. In this example, the ``transformerOutput`` and ``transformerOrderOutput`` are written in opposite conditions: if there is no ordering problem, a record is written to the former, otherwise a record is written to the latter.

Another variable is the ``input`` variable: it represents the input pipe for the filter and can likewise be used in expressions. In this example, the third output configuration (``name: transformerOrderCountOutput``) has a condition on the input being complete::

  "#input.complete"
  
The count of out of order records is written to this output when all records have been read from the input. In this way a transformer may act as an aggregator filter.

The transformer also supports a short-hand for copying an entire record from the input to the output so that all of the individual columns do not need to be copied individually::

  "#outputRecord = #inputRecord"

While the transformer is very powerful, it can also be very complex. Designs using simpler filters are easier to understand. Consider the transformer to be a special purpose filter when no other filter will do.

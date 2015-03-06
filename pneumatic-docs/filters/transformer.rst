Transformer
-----------

The transformer filter may be the most powerful filter, but also the most complex. The transformer is like an enhanced copy filter: it supports a single input, but any number of outputs. The outputs may be controlled through conditions and expressions.

The transformer uses the `Spring SpEL <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html>`_ expression language for expressions. The SpEL expressions are compiled, so they should not incur a large performance penalty.

Consider the following transformer example::

	<pipe id="input" />
	<pipe id="output1" />
	<pipe id="output2" />
	<pipe id="output3" />

	<transformer id="validatingTransformer" name="Validating Transformer">
		<input ref="input" />
		<variable name="lastName">''</variable>
		<variable name="orderingProblem">false</variable>
		<variable name="orderingProblemCount">0</variable>
		<expression>#orderingProblem = #lastName > #inputRecord.Name</expression>
		<expression>#orderingProblemCount = #orderingProblemCount + (#orderingProblem ? 1 : 0)</expression>
		<expression>#lastName = #inputRecord.Name</expression>
		<config outputName="transformerOutput" recordName="outputRecord">
			<output ref="output1" />
			<outputSchema ref="inputSchema" />
			<outputCondition>!#orderingProblem</outputCondition>
			<expression>#outputRecord.Name = #inputRecord.Name</expression>
			<expression>#outputRecord.Count = #inputRecord.Count</expression>
			<expression>#outputRecord.Price = #inputRecord.Price</expression>
		</config>
		<config outputName="transformerOrderOutput" recordName="invalidRecord">
			<output ref="output2" />
			<outputSchema ref="inputSchema" />
			<outputCondition>#orderingProblem</outputCondition>
			<expression>#invalidRecord.Name = #inputRecord.Name</expression>
			<expression>#invalidRecord.Count = #inputRecord.Count</expression>
			<expression>#invalidRecord.Price = #inputRecord.Price</expression>
		</config>
		<config outputName="transformerOrderCountOutput" recordName="invalidCountRecord">
			<output ref="output3" />
			<outputSchema ref="countSchema" />
			<outputCondition>#input.complete</outputCondition>
			<expression>#invalidCountRecord.Count = #orderingProblemCount</expression>
		</config>
	</transformer>

The first elements are pipes: one input (``id="input"``) and some outputs (``id="output1"``, ``id="output2"``, ``id="output3"``) that will be outputs from the transformer.

Next, the transformer is declared (``id="transformer"``).

The first element of the transformer is its single input::

	<input ref="input" />

Next is a set of variables. The variables may be intialized to any constant value. The initialization of the variables occurs before any records have been processed. The values of previously declared variables or input records are not available for initializiation. The variables may be used in the output configuration for both output conditions and expressions.

The variables are followed by a set of expressions. The expressions are executed for each input record processed by the filter. The following expression sets the ``orderingProblem`` boolean value to true or false, depending on if the value of ``lastName`` is alpha-numerically greater than the value of the ``Name`` column on the current input record::

	<expression>#orderingProblem = #lastName > #inputRecord.Name</expression>

This means that if, for example, ``lastName`` is "Mojo" and the value of ``inputRecord.Name`` is "Bronson", ``lastName`` will be set to "true" because "M" comes after "B" in the alphabet.

There are two forms for accessing a column on a record. One is using the "dot" notation as above: ``inputRecord.Name``. The name of the record is before the dot and the column name is after the dot. An alternative is to use brackets: "[]". This is expecially useful if the column name contains spaces. In the following example, the column name is "Column Name with Spaces"::

	<expression>#inputRecord["Column Name with Spaces"] = "A useful value"</expression>

Next in the example is a set of "output configurations". These define the outputs of the transformer. Each configuration (``config``) has two attributes. First is the ``outputName``, which defines a variable for the output pipe used by the configuration. This variable may be used in expressions. Second is the ``recordName``, which defines a variable for the record that will be output for that configuration. In this example, the ``outputName`` is not being used, but the ``recordName`` is used in each configuration::

	<expression>#outputRecord.Count = #inputRecord.Count</expression>
	
This expression sets the value of the ``Count`` column on the ``outputRecord`` to twice the value of the ``Count`` column on the ``inputRecord``. The ``inputRecord`` is an implicit variable available to all expressions, capturing the current input record of the filter.

The output condition (``outputCondition``) element controls the output of a configuration. If the value of the expression is true, the record is written to the output. Otherwise, no record is written. In this example, the ``transformerOutput`` and ``transformerOrderOutput`` are written in opposite conditions: if there is no ordering problem, a record is written to the former, otherwise a record is written to the latter.

Another variable is the ``input`` variable: it represents the input pipe for the filter and can likewise be used in expressions. In this example, the third output configuration (``outputName="transformerOrderCountOutput"``) has a condition on the input being complete::

	<outputCondition>#input.complete</outputCondition>
	
The count of out of order records is written to this output when all records have been read from the input. In this way a transformer may act as an aggregator filter.

The transformer also supports a short-hand for copying an entire record from the input to the output so that all of the individual columns do not need to be copied individually::

	<expression>#outputRecord = #inputRecord</expression>

While the transformer is very powerful, it can also be very complex. Designs using simpler filters are easier to understand. Consider the transformer to be a special purpose filter when no other filter will do.

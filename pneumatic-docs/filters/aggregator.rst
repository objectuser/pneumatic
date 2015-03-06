Aggregator
----------

The aggregator filter has a single input pipe. It also has a single output pipe: the aggregator output to which the aggregator's function is applied.

A single record is written to the after all input records have been processed from the aggregator's input.

Consider the following example::

	<pipe id="input" />
	<pipe id="output" />
	<pipe id="aggregatorOutput" />
	<aggregator id="priceAggregator" name="Price Aggregator">
		<input ref="input" />
		<inputSchema ref="inputSchema" />
		<output ref="aggregatorOutput" />
		<outputSchema ref="aggregatorSchema" />
		<function>
			<sum>
				<in>
					<column name="Price" type="decimal" />
				</in>
				<out>
					<column name="Total Price" type="decimal" />
				</out>
			</sum>
		</function>
	</aggregator>

The ``function`` element is the heart of the aggregator. In this case, we have a "sum" function (``sum``). The sum function sums the input selected by the ``in`` column and writes it to the output in the column defined in ``out``.

The available functions are:

* average
* count
* sum

That's not very many functions, but the aggregator may be extended with any function you like using a Spring Bean::

	<aggregator id="aggregator2" name="Test Aggregator 2">
		<input ref="input" />
		<inputSchema ref="inputSchema" />
		<output ref="aggregatorOutput" />
		<outputSchema ref="aggregatorSchema" />
		<function>
			<bean class="com.surgingsystems.etl.filter.function.SumFunction">
				<property name="inputColumnDefinition">
					<column name="Price" type="decimal" />
				</property>
				<property name="outputColumnDefinition">
					<column name="Total Price" type="decimal" />
				</property>
			</bean>
		</function>
	</aggregator>
	
In this case, we are referencing the underlying Java class (``SumFunction``). Any function that implements the internal Function interface may be used. That interface is::

	com.surgingsystems.etl.filter.function.Function

Aggregator
----------

The aggregator filter has a single input pipe. It also has a single output pipe: the aggregator output to which the aggregator's function is applied.

A single record is written to the after all input records have been processed from the aggregator's input.

Consider the following example::

	<etl:pipe id="input" />
	<etl:pipe id="output" />
	<etl:pipe id="aggregatorOutput" />
	<etl:aggregator id="priceAggregator" name="Price Aggregator">
		<etl:input ref="input" />
		<etl:inputSchema ref="inputSchema" />
		<etl:output ref="aggregatorOutput" />
		<etl:outputSchema ref="aggregatorSchema" />
		<etl:function>
			<etl:sum>
				<etl:in>
					<etl:column name="Price" type="decimal" />
				</etl:in>
				<etl:out>
					<etl:column name="Total Price" type="decimal" />
				</etl:out>
			</etl:sum>
		</etl:function>
	</etl:aggregator>

The ``etl:function`` element is the heart of the aggregator. In this case, we have a "sum" function (``etl:sum``). The sum function sums the input selected by the ``etl:in`` column and writes it to the output in the column defined in ``etl:out``.

The available functions are:

* average
* count
* sum

That's not very many functions, but the aggregator may be extended with any function you like using a Spring Bean::

	<etl:aggregator id="aggregator2" name="Test Aggregator 2">
		<etl:input ref="input" />
		<etl:inputSchema ref="inputSchema" />
		<etl:output ref="aggregatorOutput" />
		<etl:outputSchema ref="aggregatorSchema" />
		<etl:function>
			<bean class="com.surgingsystems.etl.filter.function.SumFunction">
				<property name="inputColumnDefinition">
					<etl:column name="Price" type="decimal" />
				</property>
				<property name="outputColumnDefinition">
					<etl:column name="Total Price" type="decimal" />
				</property>
			</bean>
		</etl:function>
	</etl:aggregator>
	
In this case, we are referencing the underlying Java class (``SumFunction``). Any function that implements the internal Function interface may be used. That interface is::

	com.surgingsystems.etl.filter.function.Function

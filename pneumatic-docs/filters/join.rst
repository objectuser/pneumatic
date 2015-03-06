
Join
----

The join filter joins two inputs based on a comparison of the records in the inputs. The inputs must be sorted according to the join (``comparator``) criteria prior to use in the join filter. This means either using a sort filter or otherwise ensuring the inputs are sorted. The sort criteria must be the same as the join criteria. Consider the following example::

	<pipe id="innerJoinOutput" />
	<join id="innerJoinFilter" name="Inner Join">
		<leftInput ref="joinInput1" />
		<rightInput ref="joinInput2" />
		<output ref="innerJoinOutput" />
		<outputSchema ref="inputSchema" />
		<comparator>
			<column name="Name" type="string" />
		</comparator>
	</join>

As stated, the two inputs (``leftInput ref="joinInput1"`` and ``rightInput ref="joinInput2"``) must be sorted using the same ordering as the ``comparator``. In this case, the inputs must be sorted by the ``Name`` column of the inputs.

The comparator is used to join the records based on the column provided. The default comparator only accepts a single column for the comparison. A single output record is created from the joined records. The output schema (``ref="inputSchema"``) is used to select the columns from the input records. If there is no record in the ``rightInput`` that matches the current record in the ``leftInput`` no output record is written.

The join filter has a second form::

	<pipe id="outerJoinOutput" />
	<join id="outerJoinFilter" name="Outer Join">
		<leftInput ref="joinInput1" />
		<rightInput ref="joinInput2" />
		<output ref="outerJoinOutput" />
		<outputSchema ref="inputSchema" />
		<comparator>
			<column name="Name" type="string" />
		</comparator>
		<leftOuterJoin />
	</join>

The difference is the ``leftOuterJoin`` element. With this element, if there is no record in the ``rightInput`` that matches a record in the ``leftInput``, the output record is still created and sent to the output.

The join filter may be extended for other forms of comparison. Creating your own comparator may require some pramming and uses the Spring bean syntax. Consider the following example::

	<join id="innerJoinWithCustomComparator" name="Inner Join with Custom Comparator">
		<leftInput ref="input1" />
		<rightInput ref="input2" />
		<output ref="output" />
		<outputSchema ref="inputSchema" />
		<comparator>
			<bean class="com.surgingsystems.etl.filter.JoinFilterTest.TwoColumnComparator">
				<property name="columns">
					<list>
						<column name="Name" type="string" />
						<column name="Count" type="integer" />
					</list>
				</property>
			</bean>
		</comparator>
	</join>
	
Here, the comparator is provided by a Spring Bean with the given class::

	<bean class="com.surgingsystems.etl.filter.JoinFilterTest.TwoColumnComparator">

The rest of the bean definition is specific to the class itsef, but note that Pneumatic elements may be used in defining the bean::

	<list>
		<column name="Name" type="string" />
		<column name="Count" type="integer" />
	</list>

The ``column`` elements are the same elements used to define schemas in Pneumatic.

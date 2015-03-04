
Join
----

The join filter joins two inputs based on a comparison of the records in the inputs. The inputs must be sorted according to the join (``etl:comparator``) criteria prior to use in the join filter. This means either using a sort filter or otherwise ensuring the inputs are sorted. The sort criteria must be the same as the join criteria. Consider the following example::

	<etl:pipe id="innerJoinOutput" />
	<etl:join id="innerJoinFilter" name="Inner Join">
		<etl:leftInput ref="joinInput1" />
		<etl:rightInput ref="joinInput2" />
		<etl:output ref="innerJoinOutput" />
		<etl:outputSchema ref="inputSchema" />
		<etl:comparator>
			<etl:column name="Name" type="string" />
		</etl:comparator>
	</etl:join>

As stated, the two inputs (``etl:leftInput ref="joinInput1"`` and ``etl:rightInput ref="joinInput2"``) must be sorted using the same ordering as the ``comparator``. In this case, the inputs must be sorted by the ``Name`` column of the inputs.

The comparator is used to join the records based on the column provided. The default comparator only accepts a single column for the comparison. A single output record is created from the joined records. The output schema (``ref="inputSchema"``) is used to select the columns from the input records. If there is no record in the ``rightInput`` that matches the current record in the ``leftInput`` no output record is written.

The join filter has a second form::

	<etl:pipe id="outerJoinOutput" />
	<etl:join id="outerJoinFilter" name="Outer Join">
		<etl:leftInput ref="joinInput1" />
		<etl:rightInput ref="joinInput2" />
		<etl:output ref="outerJoinOutput" />
		<etl:outputSchema ref="inputSchema" />
		<etl:comparator>
			<etl:column name="Name" type="string" />
		</etl:comparator>
		<etl:leftOuterJoin />
	</etl:join>

The difference is the ``etl:leftOuterJoin`` element. With this element, if there is no record in the ``rightInput`` that matches a record in the ``leftInput``, the output record is still created and sent to the output.

The join filter may be extended for other forms of comparison. Creating your own comparator may require some pramming and uses the Spring bean syntax. Consider the following example::

	<etl:join id="innerJoinWithCustomComparator" name="Inner Join with Custom Comparator">
		<etl:leftInput ref="input1" />
		<etl:rightInput ref="input2" />
		<etl:output ref="output" />
		<etl:outputSchema ref="inputSchema" />
		<etl:comparator>
			<bean class="com.surgingsystems.etl.filter.JoinFilterTest.TwoColumnComparator">
				<property name="columns">
					<list>
						<etl:column name="Name" type="string" />
						<etl:column name="Count" type="integer" />
					</list>
				</property>
			</bean>
		</etl:comparator>
	</etl:join>
	
Here, the comparator is provided by a Spring Bean with the given class::

	<bean class="com.surgingsystems.etl.filter.JoinFilterTest.TwoColumnComparator">

The rest of the bean definition is specific to the class itsef, but note that Pneumatic elements may be used in defining the bean::

	<list>
		<etl:column name="Name" type="string" />
		<etl:column name="Count" type="integer" />
	</list>

The ``etl:column`` elements are the same elements used to define schemas in Pneumatic.

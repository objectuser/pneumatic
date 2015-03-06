Sort
----

The sort filter sorts its input records according to a comparator. The sorted output is only written after all records have been read from its input. Consider the following example::

	<pipe id="input" />
	<pipe id="output" />
	<sort id="sort" name="Test Sort">
		<input ref="input" />
		<output ref="output" />
		<comparator>
			<column name="Name" type="string" />
		</comparator>
	</sort>
	
The records read from the input (``ref="input"``) are held in memory by the sort filter until it is ready to sort. Then the records are sorted using the comparator. In this case, the comparator orders the records by the ``Name`` column of the input records.

The sort filter also supports arbitrary comparators using Spring Beans::

	<pipe id="input" />
	<pipe id="output" />
	<sort id="sortWithBean" name="Test Sort">
		<input ref="input" />
		<output ref="output" />
		<comparator>
			<bean class="com.surgingsystems.etl.filter.SortFilterTest.TwoColumnComparator">
				<property name="column1">
					<column name="Name" type="string" />
				</property>
				<property name="column2">
					<column name="Price" type="decimal" />
				</property>
			</bean>
		</comparator>
	</sort>

In this example, the comparator is supplied by a Java class (``bean class="..."``) that has two properties for columns.

Because the sort filter stores all of its records in memory before it sorts, large data sets may use more memory than desireable. The sorting of very large data sets may be more suited to other solutions. For example, if the records are stored in a database, reading the records in sorted order might be a more appropirate solution.


Mapper
------

The mapper translates records that conform to an input schema to records that conform to an output schema. In its simplest form, the output schema provides enough information to perform the mapping. Consider the following example::

	<etl:schema id="giantBikesSchema" name="Giant Bikes Schema">
		<etl:column name="name" type="string" />
		<etl:column name="bike_number" type="integer" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="name" type="string" />
		<etl:column name="bike_number" type="string" />
		<etl:column name="year" type="integer" />
	</etl:schema>

	<etl:pipe id="input" />
	<etl:pipe id="mapperOutput" />
	<etl:mapper id="simpleMapper" name="Simple Mapper">
		<etl:input ref="input" />
		<etl:inputSchema ref="giantBikesSchema" />
		<etl:output ref="mapperOutput" />
		<etl:outputSchema ref="mtbSchema" />
	</etl:mapper>
	
The mapper filter (``mapper id="simpleMapper"``) uses the output schema (``ref="mtbSchema"``) to choose columns from the input records. The columns are chosen by name. In this example, the input has four columns available and three are selected for output: ``name``, ``bike_number`` and ``year``.

Also note that the type of the ``bike_number`` column is changed from integer to string. The mapper is able to make reasonable type conversions automatically: integer and decimal to string and integer to decimal.

More complicated translations are also supported as shown in the following example::

	<etl:schema id="giantBikesSchema" name="Giant Bikes Schema">
		<etl:column name="name" type="string" />
		<etl:column name="bike_number" type="integer" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="model_name" type="string" />
		<etl:column name="model_number" type="string" />
		<etl:column name="model_year" type="integer" />
		<etl:column name="unit_price" type="decimal" />
	</etl:schema>

	<etl:pipe id="mapperOutput" />
	<etl:mapper id="mapper" name="Mapper">
		<etl:input ref="input" />
		<etl:inputSchema ref="giantBikesSchema" />
		<etl:output ref="mapperOutput" />
		<etl:outputSchema ref="mtbSchema" />
		<etl:mappings>
			<etl:mapping>
				<etl:from>
					<etl:column name="name" type="string" />
				</etl:from>
				<etl:to>
					<etl:column name="model_name" type="string" />
				</etl:to>
			</etl:mapping>
			<etl:mapping>
				<etl:from>
					<etl:column name="bike_number" type="integer" />
				</etl:from>
				<etl:to>
					<etl:column name="model_number" type="string" />
				</etl:to>
			</etl:mapping>
			<etl:mapping>
				<etl:from>
					<etl:column name="year" type="integer" />
				</etl:from>
				<etl:to>
					<etl:column name="model_year" type="integer" />
				</etl:to>
			</etl:mapping>
			<etl:mapping>
				<etl:from>
					<etl:column name="cost" type="decimal" />
				</etl:from>
				<etl:to>
					<etl:column name="unit_price" type="decimal" />
				</etl:to>
			</etl:mapping>
		</etl:mappings>
	</etl:mapper>

The ``etl:mappings`` element allows for explicit mappings between input and output columns using the ``etl:from`` and ``etl:to`` elements. With this form, the column names do not need to match and type conversion is still supported.


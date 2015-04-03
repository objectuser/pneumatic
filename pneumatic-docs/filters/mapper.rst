.. _mapper:

Mapper
------

The mapper translates records that conform to an input schema to records that conform to an output schema. In its simplest form, the output schema provides enough information to perform the mapping. Consider the following example::

  <schema id="giantBikesSchema" name="Giant Bikes Schema">
    <column name="name" type="string" />
    <column name="bike_number" type="integer" />
    <column name="year" type="integer" />
    <column name="cost" type="decimal" />
  </schema>

  <schema id="mtbSchema" name="MTB Schema">
    <column name="name" type="string" />
    <column name="bike_number" type="string" />
    <column name="year" type="integer" />
  </schema>

  <pipe id="input" />
  <pipe id="mapperOutput" />
  <mapper id="simpleMapper" name="Simple Mapper">
    <input ref="input" />
    <inputSchema ref="giantBikesSchema" />
    <output ref="mapperOutput" />
    <outputSchema ref="mtbSchema" />
  </mapper>
  
The mapper filter (``mapper id="simpleMapper"``) uses the output schema (``ref="mtbSchema"``) to choose columns from the input records. The columns are chosen by name. In this example, the input has four columns available and three are selected for output: ``name``, ``bike_number`` and ``year``.

Also note that the type of the ``bike_number`` column is changed from integer to string. The mapper is able to make reasonable type conversions automatically: integer and decimal to string and integer to decimal.

More complicated translations are also supported as shown in the following example::

  <schema id="giantBikesSchema" name="Giant Bikes Schema">
    <column name="name" type="string" />
    <column name="bike_number" type="integer" />
    <column name="year" type="integer" />
    <column name="cost" type="decimal" />
  </schema>

  <schema id="mtbSchema" name="MTB Schema">
    <column name="model_name" type="string" />
    <column name="model_number" type="string" />
    <column name="model_year" type="integer" />
    <column name="unit_price" type="decimal" />
  </schema>

  <pipe id="mapperOutput" />
  <mapper id="mapper" name="Mapper">
    <input ref="input" />
    <inputSchema ref="giantBikesSchema" />
    <output ref="mapperOutput" />
    <outputSchema ref="mtbSchema" />
    <mappings>
      <mapping>
        <from>
          <column name="name" type="string" />
        </from>
        <to>
          <column name="model_name" type="string" />
        </to>
      </mapping>
      <mapping>
        <from>
          <column name="bike_number" type="integer" />
        </from>
        <to>
          <column name="model_number" type="string" />
        </to>
      </mapping>
      <mapping>
        <from>
          <column name="year" type="integer" />
        </from>
        <to>
          <column name="model_year" type="integer" />
        </to>
      </mapping>
      <mapping>
        <from>
          <column name="cost" type="decimal" />
        </from>
        <to>
          <column name="unit_price" type="decimal" />
        </to>
      </mapping>
    </mappings>
  </mapper>

The ``mappings`` element allows for explicit mappings between input and output columns using the ``from`` and ``to`` elements. With this form, the column names do not need to match and type conversion is still supported.


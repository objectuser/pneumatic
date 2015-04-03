.. _aggregator:

Aggregator
==========

Description
-----------

The aggregator filter has a single input pipe. It also has a single output pipe: the aggregator output to which the aggregator's function is applied.

A single record is written to the after all input records have been processed from the aggregator's input.

Consider the following example::

  input: !pipe
  aggregatorOutput: !pipe
  aggregator: !aggregator
    name: Test Aggregator
    input: ->input
    output: ->aggregatorOutput
    outputSchema: ->aggregatorSchema
    function: !sum
      in:
        name: Price
        type: decimal
      out:
        name: Total Price
        type: decimal

Here's the same example in XML::

  <pipe id="input" />
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

If you're using the YAML DSL, first define the function in Spring XML::

  <bean id="myAverageFunction" class="com.surgingsystems.etl.filter.function.AverageFunction">
    <property name="inputColumnDefinition">
      <etl:column name="Count" type="integer" />
    </property>
    <property name="outputColumnDefinition">
      <etl:column name="Average" type="decimal" />
    </property>
  </bean>

Then simply reference the function::

  input: !pipe
  aggregatorOutput: !pipe
  aggregator: !aggregator
    name: Test Aggregator
    input: ->input
    inputSchema: ->inputSchema
    output: ->aggregatorOutput
    outputSchema: ->aggregatorSchema
    function: ->myAverageFunction

In this case, we are referencing the underlying Java class (``SumFunction``). Any function that implements the internal Function interface may be used. That interface is::

  com.surgingsystems.etl.filter.function.Function

Specification
-------------

**aggregator**
  Perform an aggregation function on input records, write the result to the output

Markup:

* YAML: ``!aggregator``
* XML: ``<aggregator />``

Properties:

``name``
  the name of the filter, used in logging

``input``
  the pipe providing records into the filter 

``inputSchema`` : optional
  the schema of the records coming into the filter

  * if supplied, the value of the ``in`` property is matched against the schema to ensure compatibility
  * if supplied, input records are validated against the schema and rejected if the validation fails

``output``
  the pipe to which output records are written

``outputSchema`` : optional
  the schema of the records written to ``output``

  * if supplied, the value of the ``out`` property is matched against the schema to ensure compatibility
  
``function``
  the aggregation function of the filter

``rejection`` : optional
  the strategy for rejected records

  ``output``
    supply a pipe for the rejected records
    
  ``log`` : default
    log the rejected records
    
    ``name``
      the name of the logger (sometimes called the "category")
        
    ``level``
      the logging level; one of: ``OFF``, ``FATAL``, ``ERROR``, ``WARN``, ``INFO``, ``DEBUG``, ``TRACE``, ``ALL`` (see the `Log4J2 reference <https://logging.apache.org/log4j/2.0/log4j-api/apidocs/index.html>`_ for additional information)

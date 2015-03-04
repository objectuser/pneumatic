RESTful Lookup
--------------

The RESTful lookup is a filter that allows a Pneumatic to perform a request against a resource and use the result of the request for subsequent processing. Consider the following example::

	<etl:restfulLookup id="restfulLookup" name="Price Lookup">
		<etl:http method="GET" url="http://localhost:8080/mtb/{name}/price" />
		<etl:input ref="input" />
		<etl:inputSchema ref="inputSchema" />
		<etl:output ref="output" />
		<etl:outputSchema ref="outputSchema" />
		<etl:responseSchema ref="responseSchema" />
	</etl:restfulLookup>
	
In this example, the filter might be adding a price to the records it writes to its output.

In this case, RESTful lookup uses an HTTP GET to perform the request (``method="GET"``).

The request URL may use values from the current record. Consider the following as the ``inputSchema``::

	<etl:schema id="inputSchema" name="Input Schema">
		<etl:column name="name" type="string" />
		<etl:column name="count" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

The value of the ``name`` column would replace the token ``{name}`` in ``http://localhost:8080/mtb/{name}/price``. This is a feature of the [Spring Framework client](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/remoting.html#rest-client-access).

Now consider the following schemas::

	<etl:schema id="outputSchema" name="Input Schema">
		<etl:column name="name" type="string" />
		<etl:column name="count" type="integer" />
		<etl:column name="cost" type="decimal" />
		<etl:column name="price" type="decimal" />
	</etl:schema>

	<etl:schema id="responseSchema" name="Criteria Schema">
		<etl:column name="price" type="string" />
	</etl:schema>

The response schema from the example (``etl:responseSchema ref="responseSchema"``) reads the price from the GET request to the request URL. The values declared in that schema are available to map to the record written to the output. The output schema (``etl:outputSchema ref="outputSchema"``) is used to select values from a combination of the input record (defined by the input schema, ``etl:inputSchema ref="inputSchema"``) and the response.

Note that the data type of ``price`` in the response schema is ``string``, while the same column name in the output schema has a ``decimal`` type. Type conversion is automatic.

RESTful Writer
--------------

The RESTful writer is a filter for submitting requests to a RESTful service. Consider the following example::

	<etl:pipe id="input" />
	<etl:pipe id="rejectionOutput" />
	<etl:restfulWriter id="restfulWriter" name="Restful Writer">
		<etl:http method="PUT" url="http://localhost:8080/mtb/{name}/price" />
		<etl:input ref="input" />
		<etl:inputSchema ref="inputSchema" />
		<etl:rejection>
			<etl:output ref="rejectionOutput" />
		</etl:rejection>
	</etl:restfulWriter>

The HTTP method used to submit the request is configurable (``method="PUT"``). The payload of the request is a JSON message based on the input schema (``ref="inputSchema"``).

The URL may also contain values from the current input record, as in the RESTful lookup filter.

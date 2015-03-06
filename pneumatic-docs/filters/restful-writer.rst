RESTful Writer
--------------

The RESTful writer is a filter for submitting requests to a RESTful service. Consider the following example::

	<pipe id="input" />
	<pipe id="rejectionOutput" />
	<restfulWriter id="restfulWriter" name="Restful Writer">
		<http method="PUT" url="http://localhost:8080/mtb/{name}/price" />
		<input ref="input" />
		<inputSchema ref="inputSchema" />
		<rejection>
			<output ref="rejectionOutput" />
		</rejection>
	</restfulWriter>

The HTTP method used to submit the request is configurable (``method="PUT"``). The payload of the request is a JSON message based on the input schema (``ref="inputSchema"``).

The URL may also contain values from the current input record, as in the RESTful lookup filter.

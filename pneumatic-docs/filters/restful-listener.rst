.. _restful-listener:

RESTful Listener
----------------

(Should this be called the "RESTful Reader" to be more consistent with other filters? Something else? I'd like your feedback.)

The RESTful listener is a filter that accepts RESTful requests. The requests must use the POST HTTP method and send JSON as the request body. Consider the following example::

  <schema id="mtbSchema" name="MTB Schema">
    <column name="name" type="string" />
    <column name="year" type="integer" />
    <column name="cost" type="decimal" />
  </schema>

  <pipe id="restfulListenerOutput" />
  <restfulListener id="restfulListener" name="Restful Input">
    <path value="mtb" />
    <output ref="restfulListenerOutput" />
    <outputSchema ref="mtbSchema" />
  </restfulListener>

The schema (``id="mtbSchema"``) is shown here because the RESTful listener uses the schema to convert JSON to a record. For the schema above, the following is an example message::

  {"name": "Mojo HDR", "year": "2015", "cost": "3000.00"}

Note that the names and the data types conform to the declared schema.

The restful listener (``restfulListener id="restfulListener"``) declares a path (``path value="mtb"``) that tells Pneumatic the resource that triggers this listener. If this service is running on ``localhost:8080``, perhaps using the Pneumatic.IO Boot Runner (reference), which leverages Spring Boot (reference), a request is posted to ``http://localhost:8080/mtb`` will trigger this listener.

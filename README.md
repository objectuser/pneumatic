
<title>Pneumatic.IO</title>

# Quick Intro

Pneumatic.IO is a fresh approach to ETL and structured IO. It's a development platform, but little programming is required. This section provides a preview of Pneumatic. The concepts referenced here will be explained in the rest of this guide, but this section should provide a flavor of Pneumatic.

Pneumatic is declarative, using an XML markup  based on Spring's support for extensible markup. I hope in the future there will be a GUI for creating Pneumatic jobs. But for now, the XML provides a proof of concept that still makes creating ETL jobs really easy, but there is a dependency on understanding something of how Spring declares beans.

As a quick example, reading from a file and writing to a database might look like this (omitting some boilerplate XML):

```xml
	<jdbc:embedded-database id="dataSource" type="HSQL">
		<jdbc:script location="classpath:db-schema.sql" />
		<jdbc:script location="classpath:db-test-data.sql" />
	</jdbc:embedded-database>

	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="name" type="string" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>

	<etl:pipe id="fileReaderOutput" />
	<etl:fileReader id="fileReader" name="File Reader">
		<etl:fileResource location="classpath:data/mtb.txt" />
		<etl:output ref="fileReaderOutput" />
		<etl:outputSchema ref="mtbSchema" />
	</etl:fileReader>

	<etl:databaseWriter id="databaseWriter" name="Database Writer">
		<etl:input ref="fileReaderOutput" />
		<etl:inputSchema ref="mtbSchema" />
		<etl:dataSource ref="dataSource" />
		<etl:insertInto table-name="mtb" />
	</etl:databaseWriter>
```

The first declaration (id="dataSource") is a Spring embedded data source. Next, (id="mtbSchema") is a schema declaration used to declare the structure of records in the job. A pipe (id="fileReaderOutput") provides a conduit from one processing element (called "filters") to another. A file reader (id="fileReader") reads a file, creating records and sending them to the pipe referenced in its "output". A database writer (id="databaseWriter") writes records from the pipe referenced in its "input" to a table available in the data source.

Declaring these elements provides Pneumatic enough information to read all the records in the file and write them to the database. When there are no more records to process, Pneumatic shuts down. That's it.

# About

I created Pneumatic because I wanted to see if I could. I had the idea some years ago. Then I attended a conference where companies where showing their cloud integration solutions. That renewed my interested and so I finally tried out my idea.

I thought I could create a simple model for creating ETL and structured IO solutions. I knew I didn't have the skill to replicate some of the scalability features of the best modern ETL tools. I did, however, think I could create a platform with simple elements most people could understand and use to provide simple and useful data integrations.

I did not anticipate the impact of my decision to use Spring Framework to build Pnematic. I have used Spring a lot and it just made sense. Well, that decision has paid off over and over. Spring enabled the creation of RESTful interactions based on some very simple rules. It enabled a domain specific language for ETL, which is expressive, though not as good as a GUI. It enabled some unexpected (but obvious in retrospect) extensibility. It made creating tests, in Java and in the DSL, natural. It made creating a microservice incredibly easy.

Pneumatic doesn't compete with those enterprise-scale ETL tools on the market. Pneumatic is about simple and powerful integrations running on their own, as a service, as part of a larger program. Pneumatic is pretty light weight and can run in a container like Tomcat or from Spring Boot. I think that's awesome and hope you will find it useful.

# Current State

Pneumatic is in the "proof of concept" stage of development. It can already do very useful things, but I recommend you be conservative in your uses of it. With your feedback, however, it should progress to wider usage scenarios quickly. I encourage you to try it out and provide your feedback on the official forum.

# License

Pneumatic is licensed under the [GNU Affero General Public License](http://www.gnu.org/licenses/agpl-3.0.html). To acquire ETL under a different license, contact [Surging Systems](http://www.surgingsystems.com).

# Concepts

## Jobs and Filters

Pneumatic "programs" are composed into "jobs". Jobs read from files, from databases, from services and write to those same things. They transform data, join it, filter it, aggregate it and more.

The reading, writing, transforming, etc. are done in "filters". Filters are connected through pipes that carry the data from one filter to the next. This [pipes and filters](http://www.eaipatterns.com/PipesAndFilters.html) architecture is widely-known. It's also conceptually simple, yet quite powerful, and is the fundamental pattern of Pneumatic jobs.

These concepts are fundamental, but the words "job" and "filter" don't often appear in Pneumatic configurations. But as we discuss Pneumatic in this guide, we will use those terms, so you stil need to understand them. 

### Running a Job

Jobs may be run in a variety of ways. The most basic of these is the command line runner. If your job is contained in "job.xml", you can run it with:

```
java -jar pneumatic.jar com.surgingsystems.etl.XmlRunner job.xml
```

If you have, for example, RESTful services provided by your job, use the Spring Boot runner:

```
java -jar pneumatic.jar com.surgingsystems.etl.BootRunner job.xml
```

The boot runner will stay resident until Spring Boot shuts down.

## Schemas

Schemas describe the structure of data. Filters use schemas for validating and generating structured data in the form of "records". A record may be thought of as an "instance" of a schema. A schema is to a record like a car is to a 2015 Porsche 911.

From the previous example, a schema is defined using the "etl:schema" tag. A schema may have one to any number of columns, each with a name and a type. Currently, Pneumatic only supports "string" (Java String), "integer" (Java Integer) and "decimal" (Java Double) column types.

```xml
	<etl:schema id="mtbSchema" name="MTB Schema">
		<etl:column name="name" type="string" />
		<etl:column name="year" type="integer" />
		<etl:column name="cost" type="decimal" />
	</etl:schema>
```

If a record does not need to be validated or generated, no schema is required by the filter. An example of this is the copy filter which copies the input records to the configured outputs, unmodified.

Sometimes a schema is optional. If the schema is not provided, information is derived from other configuration elements. If the schema is supplied, it can be used to validate the configuration of the filter. This requires slightly more configuration, but schema information in validated before a job starts, so supplying a schema can save time spent processing with an invalid configuration.

### Schema Types

There are three types of schemas:
* Tabular schemas (etl:schema) represent traditional (i.e., database like) schemas with fixed columns and data types.
* XML schemas (etl:xmlSchema) represent schemas for XML documents based on the XML Schema Definition.
* JSON schemas (etl:jsonSchema) represent schemas for JSON documents based on http://json-schema.org/.

## Pipes

Pipes are conduits for data. Pipes hold data after it is processed by a filter, until it is extracted by another filter. Pipes have a fixed (and configurable) capacity. When the capacity is reached, a filter putting a record into a pipe will block until the filter on the other end of the pipe removes a record.

## Filters

Filters are where the action is. Filters perform the processing in a job. There are filters that perform basic IO with files, databases, RESTful end points, etc. There are also filters for joining data sources, transforming it, converting data types, etc.

This section describes each kind of filter.

# Examples



# Filter Reference

## Aggregator

The aggregator filter has a single input pipe. It has two output pipes: the normal output with the unchanged input record and the aggregator output to which the aggregator's function is applied. The normal output is not required for an aggregator: rather it is a convenience to the developer. The aggregator output is, however, required, as that represents the purpose of the aggregator.

Records are written to the normal output as each record is processed. Records are written to the aggregator output only after all input records have been processed.

## Copy

The copy filter has a single input pipe. It may have any number of output pipes. Each record on the input is written to each output.

## Database Lookup

## Database Reader

## Database Writer

## File Reader

## File Writer

## Funnel

## Join

## Mapper

## RESTful Consumer

## RESTful Input

## RESTful Lookup

## Sort

## Transformer

The transformer filter may be the most powerful filter, but also the most complex. Also, due to the use of an expression language for most operations, the transformer is generally slower than other stages. The transformer is like an enhanced copy filter: it can have only one input, but any number of outputs. The outputs are controlled through conditions and expressions.

The transformer uses the [Spring SpEL](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html) expression language for expressions. SpEL can be compiled, so it's not a huge performance hit, but it is there.

Consider the following transformer example.

```xml
	<etl:pipe id="transformerOutput" />
	<etl:pipe id="transformerCountOutput" />
	<etl:transformer id="transformer" name="Transformer">
		<etl:input ref="fileReaderOutput" />
		<etl:variables>
			<etl:variable name="totalCount">0</etl:variable>
		</etl:variables>
		<etl:expressions>
			<etl:expression>#totalCount = #totalCount + 1</etl:expression>
		</etl:expressions>
		<etl:config outputName="transformerOutput" recordName="outputRecord">
			<etl:output ref="transformerOutput" />
			<etl:schema ref="inputSchema" />
			<etl:expression>#outputRecord.Name = #inputRecord.Name + #inputRecord.Name</etl:expression>
			<etl:expression>#outputRecord.Count = #inputRecord.Count + #inputRecord.Count</etl:expression>
			<etl:expression>#outputRecord.Price = #inputRecord.Price + #inputRecord.Price</etl:expression>
		</etl:config>
		<etl:config outputName="transformerCountOutput" recordName="countRecord">
			<etl:output ref="transformerCountOutput" />
			<etl:schema ref="countSchema" />
			<etl:outputCondition>#input.complete</etl:outputCondition>
			<etl:expression>#countRecord.Count = #totalCount</etl:expression>
		</etl:config>
	</etl:transformer>
```

The first two elements are two pipes (id="transformerOutput" and id="transformerCountOutput") that will be outputs from the transformer.

Next, is the transformer is declared (id="transformer").

The first element of the transformer is its single input that, from its name, appears to have come from reading a file. The declaration is not shown above.

```xml
<etl:input ref="fileReaderOutput" />
```

Next is a set of "output configurations" (etl:outputConfigurations) which define the outputs of the transformer. Each configuration (etl:config) may have a schema, 

## XML File Reader

# Unit Testing

ETL jobs are traditionally challenging to test. Often, the entire job must be tested as a unit, which makes it difficult to find errors and more cumbersome to setup some kinds of test cases.

Pneumatic makes unit testing possible, easy and effective. Individual filters in a job can be isolated for unit testing either using traditional Java unit testing frameworks (e.g., JUnit) or using the built in declarative unit testing facility.

## Unit Tests

## Functional Tests

## ETL Test

## Data Set

## Examples

# Extending Pneumatic

Because of it's close ties to the Spring Framework, it's easy to extend Pneumatic.

# Foundations

Pneumatic leverages [Spring](http://spring.io/) projects whenever possible, and that's a lot. There are areas where Pneumatic diverges from some frameworks like [Spring Batch](http://projects.spring.io/spring-batch/). In the future, it is possible that Pneumatic will be more closely aligned with this project and possibly others.

There are enormous benefits from leveraging Spring. Some of these are:
* The use of quality, tested frameworks and components that don't have to be reinvented.
* The extensible XML markup that makes for an easy to use DSL while enabling massive extensibility.
* The ability to extend the framework by creating Spring beans and wiring them to ETL components.
* The ability to test portions of a job in a way that more closely aligns with traditional unit testing.

# Getting Involved

Pneumatic would benefit from wider expertise than I have. Here are some thoughts on the kinds of expertise that would really benefit Pneumatic:
* ETL expertise to drive a direction on enhancing functionality to benefit more use cases
* Spring namespace expertise for creating a top-level ETL namespace and removing the need for the "etl:" prefix for everything
* Eclipse expertise to get the XML element help to show up in Eclipse editors
* Java developers for enhancements, defect resolution and better test coverage
* Anyone that wants to enhance the documentation
* Anyone with samples, examples, etc
* Smart people with ideas!

# Scaling

This section contains thoughts on scaling Pneumatic.

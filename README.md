
# Pneumatic.IO - The Open Source ETL Platform

[Pneumatic.IO](http://pneumatic.io) is a fresh approach to ETL and structured IO. It's a development platform, but little to no programming is required.

Pneumatic is declarative, using either a custom YAML markup, or XML markup based on Spring's support for extensible markup. They work equally well, but the YAML syntax is much easier to read, I think, and easier to write, so that's what I recommend.

As a quick example, here's how you might read from a file and write its contents to a database in Pneumatic.

First, here is a Spring data source definition (Pneumatic relies heavily on Spring under the covers):

```XML
	<jdbc:embedded-database id="dataSource" type="HSQL">
		<jdbc:script location="classpath:db-schema.sql" />
		<jdbc:script location="classpath:db-test-data.sql" />
	</jdbc:embedded-database>
```

Next, here are the YAML-based Pneumatic declarations:

```YAML
# Declare a schema that defines our records
mtbSchema:
  name: Input Schema
  columns:
    - name: name
      type: string
    - name: year
      type: integer
    - name: cost
      type: decimal

# Declare a pipe to join the file reader and database writer
fileReaderOutput:
# Declare a file reader to read from mtb.txt
mtbFileReader:
  name: File Reader
  fileResource: data/mtb.txt
  output: fileReaderOutput
  outputSchema: inputSchema

# Declare a database writer to read from the pipe and write to the mtb table
mtbDatabaseWriter:
  name: Database Writer
  input: fileReaderOutput
  inputSchema: mtbSchema
  dataSource: dataSource # Reference the data source declared in Spring XML
  insertInto: mtb
```

Visually, the job looks like this:

![Simple Job](http://pneumatic.io/pneumatic/_images/SimpleJob.png)

The first declaration (id="dataSource") is a Spring embedded data source. A data source is an object that provides connections to a database like Oracle, SQL Server, MySQL, etc.

Next is a schema declaration (`mtbSchema`)  used to declare the structure of records in the job. A pipe (`fileReaderOutput`) provides a conduit from one processing element (called "filters") to another. A file reader (`mtbFileReader`) reads a file, creating records and sending them to the pipe referenced in its `output`.

A database writer (`mtbDatabaseWriter`) writes records from the pipe referenced in its `input` to a table available in the data source: the `mtb` table referenced in the `insertInto` property in this case. The `mtb` table must share the same schema as the `input`.

Declaring these elements provides Pneumatic enough information to read all the records in the file and write them to the database. When there are no more records to process, Pneumatic shuts down. That's it.

Pneumatic can do much more than this. It can:
* validate data
* aggregate data (counting, summation, averages, or anything else)
* read from and write to RESTful endpoints
* transform data

Ready for more? Read the full docs [here](http://pneumatic.io/pneumatic/) and get updates [here](http://pneumatic.io/).

Ready to get started? Clone this repository and run one of the sample jobs:

```bash
>pwd
../pneumatic-samples
>sh pn.sh run configs/totals-job.yml
```
That will build Pneumatic if it's not already built and then run the job.

Next, look at the `configs/totals-job.yml` file and read through it with the documentation handy. Pneumatic jobs are pretty easy to understand.

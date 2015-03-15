
drop table if exists mtb;
drop table if exists hospital;

create table mtb (
  name varchar(128),
  year integer,
  cost decimal
);

create table hospital (
  ProviderID varchar(512),
  HospitalName varchar(512),
  Address varchar(512),
  City varchar(512),
  State varchar(512),
  ZIPCode varchar(512),
  CountyName varchar(512),
  PhoneNumber varchar(512),
  HospitalCondition varchar(512),
  MeasureID varchar(512),
  MeasureName varchar(512),
  Score varchar(512),
  Sample varchar(512),
  Footnote varchar(512),
  MeasureStartDate varchar(512),
  MeasureEndDate varchar(512)
);
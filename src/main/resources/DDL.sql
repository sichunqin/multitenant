CREATE TABLE (
	id bigint PRIMARY KEY,
	driverclassname VARCHAR(255),
	url VARCHAR(255),
	name VARCHAR(255),
	username VARCHAR(255),
	password VARCHAR(255),
	initialize BOOLEAN
);


INSERT INTO DATASOURCECONFIG VALUES (1, 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://192.168.70.40:1433;Databasename=test1', 'test1', 'sa', 'sa123!',0);
INSERT INTO DATASOURCECONFIG VALUES (2, 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://192.168.70.40:1433;Databasename=test2', 'test2', 'sa', 'sa123!',0);

create database test1;
create database test2;
use test1;
create table city(id bigint, name varchar(200));
use test2;
create table city(id bigint, name varchar(200));



#BookShelf app 
[![Build Status](https://travis-ci.org/ppszczepaniak/BookShelf.svg?branch=master)](https://travis-ci.org/ppszczepaniak/BookShelf)

Two implementations of database:
Java ArrayList or PostgreSQL 

Commands for creating basic PostgreSQL database (one table):
```
create table books
(
   book_id bigint not null,
   title character varying(256) NOT NULL,
   author character varying(256) NOT NULL,
   pages_sum integer NOT NULL,
   year_of_published integer,
   publishing_house character varying(256)
);
```
Adding  primary key:
```
ALTER TABLE books ADD CONSTRAINT book_id_pk PRIMARY KEY (book_id);
```

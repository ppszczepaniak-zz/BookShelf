## BookShelf app 

[![Build Status](https://travis-ci.org/ppszczepaniak/BookShelf.svg?branch=master)](https://travis-ci.org/ppszczepaniak/BookShelf)

Two implementations of database available -
Java ArrayList or PostgreSQL.   
Choose implementation by commenting out one line in BookController.java:  
Use StaticListBookStorageImpl (ArrayList) or PostgresBookStorageImpl (SQL)  
***
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
***
How to test it with Postman:  

      1. POST http://localhost:8080/book/add 
        + in BODY: fill some JSON, see bookExample.txt
      2. GET http://localhost:8080/book/getAll
      3. GET http://localhost:8080/book/get 
        + in PARAMS: add bookId, value: [number - first add book, then find ID by getAll]
        
See bookExample.txt for examples of books in JSON
#BookShelf app 
[![Build Status](https://travis-ci.org/pedro-programator/BookShelf.svg?branch=master)](https://travis-ci.org/pedro-programator/BookShelf)

https://platform.cschool.pl/student-app/courses/277/modules/2845/lessons/3380/pages/1


Postgres SQL - create database via these commands:

create table books
(
   book_id bigint not null,
   title character varying(256) NOT NULL,
   author character varying(256) NOT NULL,
   pages_sum integer NOT NULL,
   year_of_published integer,
   publishing_house character varying(256)
);




ALTER TABLE books ADD CONSTRAINT book_id_pk PRIMARY KEY (book_id);

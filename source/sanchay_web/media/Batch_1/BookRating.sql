/* Create database if it does not exist */
create database if not exists BookRating;

/* Select Database */
use BookRating;

/* Delete the tables if they already exist */
drop table if exists Book;
drop table if exists Person;
drop table if exists Rating;

/* Create the schema for our tables */
create table Book(bID int, title text, year int, author text);
create table Person(pID int, name text);
create table Rating(pID int, bID int, stars int, ratingDate date);

/* Populate the tables with our data */
insert into Book values(101, 'AAA', 1999, 'SSA');
insert into Book values(102, 'AAB', 2005, 'SSB');
insert into Book values(103, 'AAC', 1989, 'VWX');
insert into Book values(104, 'AAD', 1988, 'SSS');
insert into Book values(105, 'AAE', 1997, 'STU');
insert into Book values(106, 'AAF', 2010, null);
insert into Book values(107, 'AAG', 2009, 'STU');
insert into Book values(108, 'AAH', 2011, 'SSS');

insert into Person values(201, 'ABC');
insert into Person values(202, 'DEF');
insert into Person values(203, 'GHI');
insert into Person values(204, 'JKL');
insert into Person values(205, 'MNO');
insert into Person values(206, 'PQR');
insert into Person values(207, 'STU');
insert into Person values(208, 'VWX');

insert into Rating values(201, 101, 2, '2012-01-22');
insert into Rating values(201, 101, 4, '2012-01-27');
insert into Rating values(202, 106, 4, null);
insert into Rating values(203, 103, 2, '2012-01-20');
insert into Rating values(203, 108, 4, '2012-01-12');
insert into Rating values(203, 108, 2, '2012-01-30');
insert into Rating values(204, 101, 3, '2012-01-09');
insert into Rating values(205, 103, 3, '2012-01-27');
insert into Rating values(205, 104, 2, '2012-01-22');
insert into Rating values(205, 108, 4, null);
insert into Rating values(206, 107, 3, '2013-01-15');
insert into Rating values(206, 106, 5, '2013-01-19');
insert into Rating values(207, 107, 5, '2013-01-20');
insert into Rating values(208, 104, 3, '2013-01-02');


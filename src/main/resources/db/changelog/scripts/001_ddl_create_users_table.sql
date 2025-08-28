create table if not exists users(
    id serial primary key,
    login varchar(30) not null unique,
    password varchar(30) not null
);
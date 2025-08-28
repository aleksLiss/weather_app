create table if not exists locations(
    id serial primary key,
    name varchar(20) not null,
    user_id int references users(id),
    latitude decimal not null,
    longitude decimal not null
);
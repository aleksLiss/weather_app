create table if not exists sessions(
    id UUID primary key,
    user_id int references users(id),
    expires_at timestamp not null
);
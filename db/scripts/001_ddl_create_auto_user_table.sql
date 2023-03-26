create table if not exists auto_user
(
    id       serial primary key,
    login    varchar not null UNIQUE,
    password varchar not null
);
create table if not exists files
(
    id           serial primary key,
    name         varchar                       not null,
    path         varchar                       not null unique,
    auto_post_id int references auto_post (id) not null
);
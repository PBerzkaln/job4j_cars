CREATE TABLE if not exists car
(
    id        SERIAL PRIMARY KEY,
    name      varchar not null,
    engine_id int not null references engine(id)
);
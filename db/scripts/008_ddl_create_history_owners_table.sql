create table if not exists history_owner
(
    id       serial primary key,
    startAt  TIMESTAMP WITHOUT TIME ZONE DEFAULT now() not null,
    endAt    TIMESTAMP WITHOUT TIME ZONE DEFAULT now() not null,
    owner_id int                                       not null references owners (id),
    car_id   int                                       not null references car (id)
);
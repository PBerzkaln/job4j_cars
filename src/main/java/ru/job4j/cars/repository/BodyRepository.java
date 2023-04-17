package ru.job4j.cars.repository;

import ru.job4j.cars.model.Body;

import java.util.List;
import java.util.Optional;

public interface BodyRepository {
    Optional<Body> create(Body body);

    boolean update(Body body);

    boolean delete(int bodyId);

    List<Body> findAllOrderById();

    Optional<Body> findById(int bodyId);

    Optional<Body> findByName(String name);
}
package ru.job4j.cars.service;

import ru.job4j.cars.model.Body;

import java.util.List;
import java.util.Optional;

public interface BodyService {
    Optional<Body> save(Body body);

    boolean update(Body body);

    boolean delete(int bodyId);

    List<Body> findAllOrderById();

    Optional<Body> findById(int bodyId);

    Optional<Body> findByName(String name);
}
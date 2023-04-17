package ru.job4j.cars.repository;

import ru.job4j.cars.model.Transmission;

import java.util.List;
import java.util.Optional;

public interface TransmissionRepository {
    Optional<Transmission> create(Transmission transmission);

    boolean update(Transmission transmission);

    boolean delete(int transmissionId);

    List<Transmission> findAllOrderById();

    Optional<Transmission> findById(int transmissionId);

    Optional<Transmission> findByName(String name);
}
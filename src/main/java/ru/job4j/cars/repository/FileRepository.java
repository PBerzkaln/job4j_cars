package ru.job4j.cars.repository;

import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    Optional<File> create(File file);

    boolean update(File file);

    boolean delete(int fileId);

    List<File> findAllOrderById();

    Optional<File> findById(int fileId);

    Optional<File> findByName(String name);
}
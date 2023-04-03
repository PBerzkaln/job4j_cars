package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAllForTheLastDay();

    List<Post> findWithPhoto();

    List<Post> findByModel(String model);
}
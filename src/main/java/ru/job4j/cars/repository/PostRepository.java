package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> create(Post post);

    boolean update(Post post);

    boolean delete(int postId);

    List<Post> findAllOrderById();

    Optional<Post> findById(int postId);

    List<Post> findAllForTheLastDay();

    List<Post> findWithPhoto();

    List<Post> findByModel(String model);
    boolean setIsSold(int id);
}
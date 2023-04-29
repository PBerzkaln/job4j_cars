package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnPostRepository implements PostRepository {
    private final CrudRepository crudRepository;

    public Post create(Post post) {
        crudRepository.run((session -> session.save(post)));
        return post;
    }

    public boolean update(Post post) {
        crudRepository.run(session -> session.merge(post));
        return true;
    }

    public boolean delete(int postId) {
        return crudRepository.booleanQuery("DELETE Post WHERE id = :fId", Map.of("fId", postId));
    }

    public List<Post> findAllOrderById() {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.car JOIN FETCH p.file ORDER BY p.id ASC", Post.class);
    }

    public Optional<Post> findById(int postId) {
        return crudRepository.optional(
                "FROM Post AS p JOIN FETCH p.car JOIN FETCH p.file WHERE p.id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }

    @Override
    public List<Post> findAllForTheLastDay() {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.file JOIN FETCH p.car "
                        + "WHERE created BETWEEN :fDate24 AND :fDateNow", Post.class,
                Map.of("fDate24", LocalDateTime.now().minusDays(1),
                        "fDateNow", LocalDateTime.now())
        );
    }

    @Override
    public List<Post> findWithPhoto() {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.file JOIN FETCH p.car "
                        + "WHERE p.file.id IS NOT NULL", Post.class
        );
    }

    @Override
    public List<Post> findByModel(String model) {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.file JOIN FETCH p.car "
                        + "WHERE p.car.name = :fName", Post.class,
                Map.of("fName", model)
        );
    }

    @Override
    public boolean setIsSold(int id) {
        var mark = findById(id).get().isSold() ? Boolean.FALSE : Boolean.TRUE;
        return crudRepository.booleanQuery("UPDATE Post SET sold = :fsold WHERE id = :fId",
                Map.of("fId", id, "fsold", mark));
    }
}
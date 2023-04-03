package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HbnPostRepository implements PostRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Post> findAllForTheLastDay() {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.files JOIN FETCH p.car "
                        + "where auto_post.created > :fDate, Post.class", Post.class,
                Map.of("fDate", LocalDateTime.now().minusDays(1))
        );
    }

    @Override
    public List<Post> findWithPhoto() {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.files JOIN FETCH p.car "
                        + "WHERE auto_post_id IS NOT NULL", Post.class
        );
    }

    @Override
    public List<Post> findByModel(String model) {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.files JOIN FETCH p.car "
                        + "WHERE name = :fName", Post.class,
                Map.of("fName", model)
        );
    }
}
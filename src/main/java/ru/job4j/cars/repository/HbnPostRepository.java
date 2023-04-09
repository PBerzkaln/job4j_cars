package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnPostRepository implements PostRepository {
    private static final Logger LOG = LogManager.getLogger(HbnEngineRepository.class.getName());
    private final CrudRepository crudRepository;

    public Optional<Post> create(Post post) {
        Optional<Post> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(post)));
            rsl = Optional.of(post);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean update(Post post) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(post));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean delete(int postId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Post WHERE id = :fId",
                    Map.of("fId", postId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
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
                        + "WHERE p.file_id IS NOT NULL", Post.class
        );
    }

    @Override
    public List<Post> findByModel(String model) {
        return crudRepository.query(
                "FROM Post AS p JOIN FETCH p.file JOIN FETCH p.car "
                        + "WHERE name = :fName", Post.class,
                Map.of("fName", model)
        );
    }
}
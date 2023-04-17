package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Body;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnBodyRepository implements BodyRepository {
    private static final Logger LOG = LogManager.getLogger(HbnBodyRepository.class.getName());
    private final CrudRepository crudRepository;

    public Optional<Body> create(Body body) {
        Optional<Body> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(body)));
            rsl = Optional.of(body);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean update(Body body) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(body));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean delete(int bodyId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Body WHERE id = :fId",
                    Map.of("fId", bodyId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Body> findAllOrderById() {
        return crudRepository.query(
                "from Body order by id asc", Body.class);
    }

    public Optional<Body> findById(int bodyId) {
        return crudRepository.optional(
                "from Body where id = :fId", Body.class,
                Map.of("fId", bodyId)
        );
    }

    public Optional<Body> findByName(String name) {
        return crudRepository.optional(
                "from Body where name = :fname", Body.class,
                Map.of("fname", name)
        );
    }
}
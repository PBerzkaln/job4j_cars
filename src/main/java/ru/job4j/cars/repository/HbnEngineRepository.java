package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnEngineRepository implements EngineRepository {
    private static final Logger LOG = LogManager.getLogger(HbnEngineRepository.class.getName());
    private final CrudRepository crudRepository;

    public Optional<Engine> create(Engine engine) {
        Optional<Engine> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(engine)));
            rsl = Optional.of(engine);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean update(Engine engine) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(engine));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean delete(int engineId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Engine WHERE id = :fId",
                    Map.of("fId", engineId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Engine> findAllOrderById() {
        return crudRepository.query(
                "from Engine order by id asc", Engine.class);
    }

    public Optional<Engine> findById(int engineId) {
        return crudRepository.optional(
                "from Engine where id = :fId", Engine.class,
                Map.of("fId", engineId)
        );
    }

    public Optional<Engine> findByName(String name) {
        return crudRepository.optional(
                "from Engine where name = :fname", Engine.class,
                Map.of("fname", name)
        );
    }
}
package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnFileRepository implements FileRepository {
    private static final Logger LOG = LogManager.getLogger(HbnEngineRepository.class.getName());
    private final CrudRepository crudRepository;

    @Override
    public Optional<File> create(File file) {
        Optional<File> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(file)));
            rsl = Optional.of(file);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean update(File file) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(file));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean delete(int fileId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE File WHERE id = :fId",
                    Map.of("fId", fileId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public List<File> findAllOrderById() {
        return crudRepository.query(
                "from File order by id asc", File.class);
    }

    @Override
    public Optional<File> findById(int fileId) {
        return crudRepository.optional(
                "from File where id = :fId", File.class,
                Map.of("fId", fileId)
        );
    }

    @Override
    public Optional<File> findByName(String name) {
        return crudRepository.optional(
                "from File where name = :fname", File.class,
                Map.of("fname", name)
        );
    }
}
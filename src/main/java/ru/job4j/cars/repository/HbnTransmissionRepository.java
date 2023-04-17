package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Transmission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnTransmissionRepository implements TransmissionRepository {
    private static final Logger LOG = LogManager.getLogger(HbnTransmissionRepository.class.getName());
    private final CrudRepository crudRepository;

    public Optional<Transmission> create(Transmission transmission) {
        Optional<Transmission> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(transmission)));
            rsl = Optional.of(transmission);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean update(Transmission transmission) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(transmission));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean delete(int transmissionId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Transmission WHERE id = :fId",
                    Map.of("fId", transmissionId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Transmission> findAllOrderById() {
        return crudRepository.query(
                "from Transmission order by id asc", Transmission.class);
    }

    public Optional<Transmission> findById(int transmissionId) {
        return crudRepository.optional(
                "from Transmission where id = :fId", Transmission.class,
                Map.of("fId", transmissionId)
        );
    }

    public Optional<Transmission> findByName(String name) {
        return crudRepository.optional(
                "from Transmission where name = :fname", Transmission.class,
                Map.of("fname", name)
        );
    }
}
package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnOwnerRepository implements OwnerRepository {
    private static final Logger LOG = LogManager.getLogger(HbnOwnerRepository.class.getName());
    private final CrudRepository crudRepository;

    public Optional<Owner> create(Owner owner) {
        Optional<Owner> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(owner)));
            rsl = Optional.of(owner);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean update(Owner owner) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(owner));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean delete(int ownerId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Owner WHERE id = :fId",
                    Map.of("fId", ownerId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Owner> findAllOrderById() {
        return crudRepository.query(
                "from Owner order by id asc", Owner.class);
    }

    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional(
                "from Owner where id = :fId", Owner.class,
                Map.of("fId", ownerId)
        );
    }

    public Optional<Owner> findByName(String name) {
        return crudRepository.optional(
                "from Owner where name = :fname", Owner.class,
                Map.of("fname", name)
        );
    }
}
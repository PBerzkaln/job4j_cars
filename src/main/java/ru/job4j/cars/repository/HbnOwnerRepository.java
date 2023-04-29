package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnOwnerRepository implements OwnerRepository {
    private final CrudRepository crudRepository;

    public Owner create(Owner owner) {
        crudRepository.run((session -> session.save(owner)));
        return owner;
    }

    public boolean update(Owner owner) {
        crudRepository.run(session -> session.merge(owner));
        return true;
    }

    public boolean delete(int ownerId) {
        return crudRepository.booleanQuery("DELETE Owner WHERE id = :fId",
                Map.of("fId", ownerId));
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
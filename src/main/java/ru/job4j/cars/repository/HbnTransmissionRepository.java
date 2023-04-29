package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Transmission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnTransmissionRepository implements TransmissionRepository {
    private final CrudRepository crudRepository;

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
}
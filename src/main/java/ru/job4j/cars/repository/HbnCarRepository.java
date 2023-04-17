package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnCarRepository implements CarRepository {
    private static final Logger LOG = LogManager.getLogger(HbnCarRepository.class.getName());
    private final CrudRepository crudRepository;

    public Optional<Car> create(Car car) {
        Optional<Car> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(car)));
            rsl = Optional.of(car);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean update(Car car) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(car));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public boolean delete(int carId) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Car WHERE id = :fId",
                    Map.of("fId", carId)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Car> findAllOrderById() {
        return crudRepository.query(
                "FROM Car AS c JOIN FETCH c.engine "
                        + "JOIN FETCH c.body "
                        + "JOIN FETCH c.transmission ORDER BY c.id ASC", Car.class);
    }

    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "FROM Car AS c JOIN FETCH c.engine "
                        + "JOIN FETCH c.body "
                        + "JOIN FETCH c.transmission WHERE c.id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    public Optional<Car> findByName(String name) {
        return crudRepository.optional(
                "FROM Car AS c JOIN FETCH c.engine "
                        + "JOIN FETCH c.body "
                        + "JOIN FETCH c.transmission WHERE c.name = :fname", Car.class,
                Map.of("fname", name)
        );
    }
}